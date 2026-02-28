import axios from 'axios'
import router from "../router";
import { ElMessage } from 'element-plus';

const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json',
  }
})

// 添加请求拦截器
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, error => {
  // 不弹窗，直接 reject
  return Promise.reject(error);
});

// 添加响应拦截器
request.interceptors.response.use(
  function (response) {
    // 只在 response.data 是对象且有 code 字段时校验
    if (response.data && typeof response.data === 'object' && 'code' in response.data) {
    if (response.data.code !== 200) {
        // 不弹窗，直接 reject
      return Promise.reject(response.data);
      }
      return response;
    }
    // 否则直接返回（如数组、字符串等）
    return response;
  },
  function (error) {
    // 不弹窗，直接 reject
    return Promise.reject(error);
  }
);

export default request;

