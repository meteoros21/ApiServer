// import {store} from "@/store";
// import cookie from "js.cookie"

class PostHandler {
  // constructor()
  // {
  // }

  // getPostList2 (vue, page, rowsPerPage) {
  //   let http = vue.$http
  //   // let store = vue.$store
  //   // let token = store.getters.getUserToken;
  //   // let token = cookie.get('userToken');
  //   let token = sessionStorage.getItem('userToken')
  //   page = (typeof page === 'undefined') ? 1 : page
  //
  //   return http.get('http://localhost:8080/api/post/list?page=' + page + '&rpp=' + rowsPerPage, {
  //     headers: {
  //       'content-type': 'application/x-www-form-urlencoded;charset=UTF-8',
  //       'Authorization': 'Bearer ' + token
  //     }
  //   })
  // }

  getPostList (page, rowsPerPage) {
    var token = sessionStorage.getItem('userToken')
    var apiUrl = 'http://localhost:8080/api/post/list?page=' + page + '&rpp=' + rowsPerPage;
    var init = {
      method: 'get',
      headers: {
        'content-type': 'application/x-www-form-urlencoded;charset=UTF-8',
        'Authorization': 'Bearer ' + token
      }
    }

    return fetch(apiUrl, init)
      .then(response => response.json())
  }

  getPost (postId) {
    var token = sessionStorage.getItem('userToken')
    var apiUrl = 'http://localhost:8080/api/post/post?postId=' + postId
    var init = {
      method: 'get',
      headers: {
        'content-type': 'application/x-www-form-urlencoded;charset=UTF-8',
        'Authorization': 'Bearer ' + token
      }
    }

    return fetch(apiUrl, init)
      .then(response => response.json())
  }

  updatePost (data) {
    var token = sessionStorage.getItem('userToken')
    var apiUrl = 'http://localhost:8080/api/post/update'
    var init = {
      method: 'post',
      body: data,
      headers: {
        'content-type': 'application/x-www-form-urlencoded;charset=UTF-8',
        'Authorization': 'Bearer ' + token
      }
    }

    return fetch(apiUrl, init)
      .then(response => response.json())
  }

  deletePost (postId) {
    var token = sessionStorage.getItem('userToken')
    var apiUrl = 'http://localhost:8080/api/post/delete?postId=' + postId
    var init = {
      method: 'get',
      headers: {
        'content-type': 'application/x-www-form-urlencoded;charset=UTF-8',
        'Authorization': 'Bearer ' + token
      }
    }

    return fetch(apiUrl, init)
        .then(response => response.json())
  }
}

//export default new PostHandler()
