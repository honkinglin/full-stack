import axios from "axios";
// import qs from 'qs';
// import { message } from 'antd';

let domain = "http://127.0.0.1:8080/";


axios.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    return Promise.resolve(error.response);
  }
);

axios.defaults.withCredentials = true;

function checkStatus(response) {
  // loading
  // 如果http状态码正常，则直接返回数据
  if (
    response &&
    (response.status === 200 ||
      response.status === 304 ||
      response.status === 400)
  ) {
    return response.data;
    // 如果不需要除了data之外的数据，可以直接 return response.data
  }
  // 异常状态下，把错误信息返回去
  return {
    status: -404,
    msg: "网络异常",
  };
}

function checkCode(res) {
  // 如果code异常(这里已经包括网络错误，服务器错误，后端抛出的错误)，可以弹出一个错误提示，告诉用户
  if (res.status === -404) {
    // message.error('网络错误');
    return;
  }

  if (res.status === 10007) {
    // message.error('请重新登录');
    console.log(window.location + "/login");
    // window.open(window.host+'/login');
    // window.location.href = 'http://127.0.0.1:8080/#/login';
    window.location.href = "http://127.0.0.1:8080/#/login";
    return;
  }

  if (res.status === 9999) {
    // message.error('请重新登录');

    return;
  }
  if (res.status !== 10000) {
    // message.error(res.errorMsg);
  }
  return res;
}

export default {
  post(url, params,flag=0) {
    console.log('sessionStorage===',sessionStorage.getItem('token'))
    if(flag === 0 && !sessionStorage.getItem('token')){
      window.location.href = 'http://127.0.0.1:8080/#/login';
      return
    }
    return axios(
      {
        method: "POST",
        baseURL: domain,
        url,
        // data: JSON.stringify(data),
        params: params,
        timeout: 10000,
        headers: {
          "X-Requested-With": "XMLHttpRequest",
          'jwt_token': sessionStorage.getItem('token') || null
          // 'Content-Type': 'application/json; charset=UTF-8'
        },
        withCredentials: true,
        // credentials: 'same-origin',
      },
      { withCredentials: true }
    )
      .then((response) => {
        return checkStatus(response);
      })
      .then((res) => {
        return checkCode(res);
      });
  },
  get(url, params, flag =0) {
    console.log('sessionStorage===',sessionStorage.getItem('token'))
    if(flag === 0 && !sessionStorage.getItem('token')){
      window.location.href = 'http://127.0.0.1:8080/#/login';
    }
    return axios(
      {
        method: "get",
        baseURL: domain,
        url,
        params, // get 请求时带的参数
        timeout: 10000,
        headers: {
          "X-Requested-With": "XMLHttpRequest",
          'jwt_token': sessionStorage.getItem('token') || null
        },
        withCredentials: true,
        // credentials: 'same-origin',
      },
      { withCredentials: true }
    )
      .then((response) => {
        return checkStatus(response);
      })
      .then((res) => {
        return checkCode(res);
      });
  },
};
