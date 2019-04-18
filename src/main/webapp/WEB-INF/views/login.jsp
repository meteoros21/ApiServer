<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8" />
    <title>로그인</title>
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/login.css">
    <script src="https://cdn.jsdelivr.net/npm/vue"></script>
</head>
<body>
<div id="app">
    <div class="container">
        <div class="login-container">
            <form name="login-form" id="login_frm" method="post" @submit.prevent="login" autocomplete="off">
                <h2>Welcome to Seongho's board</h2>
                <input type="text" name="userId" v-model="userId" placeholder="Email">
                <input type="password" name="password" v-model="pwd" placeholder="Password" style="border-top:none">
                <button type="submit" style="">Sign in</button>
                <div style="width: 100%; text-align: center; margin-top: 10px">
                    <a href="#">forgot my password</a> |
                    <a href="#">sign up</a>
                </div>
                <p style="width: 100%; text-align: right; margin-top: 1.5em; color: #3c3c3c">I-ON Communications Co, LTD</p>
            </form>
        </div>
    </div>
</div>
<script>
    var app = new Vue ({
        el: '#app',
        data() {
            return {
                userId: '',
                pwd: ''
            }
        },
        methods: {
            login: function() {
                var params = {
                    username: this.userId,
                    password: this.pwd,
                    grant_type: 'password',
                    scope: 'read'
                }

                var queryString = Object.keys(params)
                    .map(k => encodeURIComponent(k) + '=' + encodeURIComponent(params[k]) ).join('&');

                // Authorization Value
                var authVal = btoa('test-api1' + ':' + '1111')

                // Fetch Option
                var init = {
                    method: 'post',
                    headers: {
                        'content-type': 'application/x-www-form-urlencoded;charset=UTF-8',
                        'Authorization': 'Basic ' + authVal
                    }
                }

                fetch('http://127.0.0.1:8081/oauth/token?' + queryString, init)
                    .then(function (response) {
                        console.log(response.ok)
                        var data = response.json()

                        if (response.ok) {
                            return data
                        } else {
                            throw response
                        }
                    })
                    .then(function (data) {
                        console.log(data);
                        var token = data['access_token'];
                        sessionStorage.setItem('userToken', token)

                        window.location.href = 'post/list'
                        //alert(token);
                        // alert(data['refresh_token']);

                        // cookies.set('userToken', token, {expires: 1});
                        // store.commit('setUserToken', {userToken: token});

                        // alert(store.getters.getUserToken);
                    })
                    .catch(function (response) {
                        console.log(response)
                        alert('Fail to get access token' + response)
                    })
            }
        }
    })
</script>
</body>