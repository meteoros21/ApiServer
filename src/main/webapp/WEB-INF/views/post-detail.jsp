<%@ taglib prefix="v-on" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8" />
    <title>게시판</title>
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/post.css">
    <script src="https://cdn.jsdelivr.net/npm/vue"></script>
    <script src="/js/PostHandler.js"></script>
</head>
<body>
<div id="app">
    <div class="container">
        <h1>Post Detail</h1>
        <table class="tbl-post-detail">
            <tr>
                <td class="title">{{post.title}}</td>
                <td class="writer">{{post.user != null ? post.user.userName: ""}}({{post.regTimeString}})</td>
            </tr>
            <tr>
                <td colspan="2" class="td-post-content"><p v-html="post.body"></p></td>
            </tr>
        </table>
        <button v-on:click="back" class="btn-detail">Back to List</button>
        <button v-on:click="edit" class="btn-detail">Update</button>
    </div>
</div>
<script>
    var postHandler = new PostHandler();
    var urlParams = new URLSearchParams(window.location.search);
    var postId = urlParams.get('postId');

    var app = new Vue({
        el: '#app',
        data () {
            return {
                post: {}
            }
        },
        methods: {
            back: function () {
                window.history.back();
            },
            edit: function () {
                window.location.href = 'edit?postId=' + postId;
            }
        },
        created () {
            let fetchData = postHandler.getPost(postId)
            fetchData.then((post) => {
                if (typeof post.error != 'undefined') {
                    window.location.href = '/login';
                    return;
                }
                this.post = post;
                this.post.body = this.post.body.split('\r').join('<br>');
                console.log(this.post)
            })
        }
    })
</script>
</body>
</html>