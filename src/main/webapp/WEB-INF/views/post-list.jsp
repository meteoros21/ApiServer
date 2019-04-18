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
        <h1>Post List</h1>
        <table class="tbl-post-list">
            <tr>
                <th>No</th>
                <th width="60%">Title</th>
                <th>Writer</th>
                <th>Reg. Date</th>
            </tr>
            <tr v-for="post in posts" :key="post.postId">
                <td>{{post.postId}}</td>
                <td class="content"><a href="#" v-on:click="goDetailPage(post.postId)"> <router-link :to="{path:'/post-detail/' + post.postId}">{{post.title}}</router-link></a></td>
                <td>{{post.user.userName}}</td>
                <td>{{post.regDateString}}</td>
            </tr>
        </table>
        <pagination v-bind:cur-page="curPage" v-bind:rows-per-page="rowsPerPage" v-bind:row-count="rowCount"></pagination>

        <button v-on:click="goRegPage" class="btn-list">Register</button>
    </div>
</div>

<script>
    var postHandler = new PostHandler();

    // 페이지 변경 이벤트를 위한 이벤트 버스
    var eventBus = new Vue();

    var curPage = sessionStorage.getItem('postList:curPage');
    curPage = (typeof curPage == 'undefined' || curPage == null || curPage === 'null') ? 1 : curPage;

    // 페이지 네비게이션
    var Pagination = {
        template: '<template>\n' +
            '  <div class="pagination">\n' +
            '    <button type="button" @click="test(firstPage)" title="First Page">&lt;&lt;</button>\n' +
            '    <button type="button" @click="test(prevPageGroup)" title="Previous Page Group">&lt;</button>\n' +
            '    <button type="button" v-for="page in (startPage, endPage)" v-bind:class="getClassName(page)" v-bind:key="page" @click="test(page)">{{page}}</button>\n' +
            '    <button type="button" @click="test(nextPageGroup)" title="Next Page Group">&gt;</button>\n' +
            '    <button type="button" @click="test(lastPage)" title="Last Page">&gt;&gt;</button>\n' +
            '  </div>\n' +
            '</template>',
        props: ['rowCount', 'rowsPerPage', 'curPage'],
        methods: {
            test: function (p) {
                // 이벤트를 발생시킨다.
                eventBus.$emit('curPageChanged', p)
            },
            getClassName: function (page) {
                return parseInt(this.curPage) === parseInt(page) ? 'active' : ''
            }
        },
        computed: {
            startPage: function () {
                return (this.curPageGroup - 1) * 10 + 1
            },
            endPage: function () {
                let ep = this.startPage + 10
                // let pc = Math.ceil(this.rowCount / this.rowsPerPage)
                return (ep > this.pageCount) ? this.pageCount : ep
            },
            pageCount: function () {
                return Math.ceil(this.rowCount / this.rowsPerPage)
            },
            pageGroupCount: function () {
                return Math.ceil(this.pageCount / 10)
            },
            curPageGroup: function () {
                return Math.ceil(this.curPage / 10)
            },
            prevPageGroup: function () {
                return (this.curPageGroup === 1) ? 1 : (this.curPageGroup * 10 - 1)
            },
            firstPage: function () {
                return 1
            },
            nextPageGroup: function () {
                return this.curPageGroup < this.pageGroupCount ? (this.curPageGroup * 10 + 1) : this.lastPage
            },
            lastPage: function () {
                return this.pageCount
            }
        }
    }

    var app = new Vue({
        el: '#app',
        components: {
            'pagination': Pagination
        },
        data() {
            return {
                posts: [],
                curPage: curPage,
                rowCount: 0,
                rowsPerPage: 10
            }
        },
        watch: {
            'curPage' : 'getList'
        },
        methods: {
            getList: function() {
                sessionStorage.setItem('postList:curPage', this.curPage);
                var fetchData = postHandler.getPostList(this.curPage, this.rowsPerPage);
                fetchData.then((result) => {
                    if (typeof result.error != 'undefined')
                    {
                        window.location.href = '/login';
                    }
                    else
                    {
                        this.posts = result.postList
                        this.rowCount = result.rowCount
                    }
                })
            },
            goRegPage: function () {
                alert('test');
                window.location.href = 'edit';
            },
            goDetailPage: function(postId) {
                window.location.href = 'detail?postId=' + postId;
            },
            pageChanged: function (page) {
                this.curPage = page;
            }
        },
        created() {
            // 페이지 변경 이벤트를 구독한다.
            eventBus.$on('curPageChanged', this.pageChanged)

            // 게시물 리스트를 얻어 온다.
            this.getList()
        }
    })
</script>
</body>