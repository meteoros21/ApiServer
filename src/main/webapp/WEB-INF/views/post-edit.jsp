<%@ taglib prefix="v-on" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8" />
    <title>게시판</title>
    <link rel="stylesheet" href="/css/common.css">
    <link rel="stylesheet" href="/css/post.css">
    <script src="/ckeditor/ckeditor.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vue"></script>
    <script src="/js/PostHandler.js"></script>
</head>
<body>
<div id="app">
    <div class="container">
        <h1>Post Edit</h1>
        <form name="form-post" method="post" @submit.prevent="register">
            <input type="hidden" name="postId" v-model="postId">
            <input type="hidden" name="userId" v-model="userId">
            <table class="tbl-post-edit">
                <tr>
                    <td><input type="text" name="title" v-model="title"></td>
                </tr>
                <tr>
                    <td>
                        <textarea id="editor1" name="postBody" v-model="body"></textarea>
                        <script type="text/javascript">
                            setTimeout(function () {
                                CKEDITOR.replace( 'editor1', {
                                    extraPlugins: 'colorbutton,panelbutton,floatpanel,panel,colordialog,tableresize',
                                    height: '400px',
                                    fullPage: false,
                                    allowedContent: true,
                                    contentsCss: ['http://localhost:8080/ckeditor/contents.css'],
                                    filebrowserUploadUrl: '/post/upload-image'
                                });
                            }, 100)
                        </script>
                    </td>
                </tr>
            </table>
            <button type="button" v-on:click="back" class="btn-detail">Back to View</button>
            <button type="button" v-on:click="register" class="btn-detail">{{buttonLabel}}</button>
        </form>
    </div>
</div>
<script>
    var postHandler = new PostHandler();
    var urlParams = new URLSearchParams(window.location.search);
    var postId = urlParams.get('postId');
    //postId = ${postId};
    postId = (typeof postId == 'undefined' || postId == null || postId == 'null') ? 0 : postId;

    var app = new Vue({
        el: '#app',
        data: function () {
            return {
                post: {},
                postId: 0,
                userId: '',
                title: '',
                body: '',
                buttonLabel: 'Register'
            }
        },
        methods: {
            back: function () {
                window.history.back();
            },
            register: function () {
                var data = new URLSearchParams(new FormData(document.forms['form-post']))
                data.set('postBody', CKEDITOR.instances['editor1'].getData())

                var fetchData = postHandler.updatePost(data);
                fetchData.then(function (result) {
                    console.log(result);
                    if (this.buttonLabel === 'Register')
                        window.history.replaceState(null, '게시판', 'list');
                    else
                        window.history.back();
                });
            }
        },
        created () {
            if (postId > 0) {
                let fetchData = postHandler.getPost(postId)
                fetchData.then((post) => {
                    if (typeof post.error != 'undefined')
                    {
                        window.location.href = '/login';
                        return;
                    }
                    this.post = post
                    // this.post.body = this.post.body.split('\r').join('<br>')
                    this.userId = post.userId
                    this.postId = post.postId
                    this.title = post.title
                    this.body = post.body
                    this.buttonLabel = 'Update'
                })
            }
        }
    })


</script>
</body>
</html>