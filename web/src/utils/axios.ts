// axios.js
import { notification } from 'antd';
import axios from 'axios';
import { history } from 'umi';
import data from './data';

// Add a request interceptor
axios.interceptors.request.use(
  (config: any) => {
    if (
      // config.baseURL === baseApiAddress &&
      !config.headers.Authorization
    ) {
      const token = localStorage.getItem('token');
      if (token) {
        // eslint-disable-next-line no-param-reassign
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    config.headers['Content-Type'] = 'application/json';
    config.headers['Access-Control-Allow-Origin'] = '*';
    config.headers['Access-Control-Allow-Headers'] = '*';
    config.headers['Access-Control-Allow-Methods'] = '*';
    return config;
  },
  (error: any) => Promise.reject(error),
);

// Add a response interceptor
axios.interceptors.response.use(
  (response: any) => response,
  (error: any) => {
    console.log("________________________________");
    console.log(error);
    switch (error?.response?.status) {
      case 400: {
        notification.error({
          message: 'Bad request',
          description:
            data.error[
              error?.response?.data?.detail?.errorCode || error?.response?.data?.errorCode
            ] ||
            error?.response?.data?.errorDescription ||
            error?.data?.detail?.message ||
            error?.message,
        });
        break;
      }

      case 401:
        notification.error({
          message: 'Vui lòng đăng nhập lại',
          description: '',
        });
        localStorage.removeItem('vaiTro');
        localStorage.removeItem('token');
        localStorage.removeItem('accessTokens');
        localStorage.removeItem('phuongThuc');
        localStorage.removeItem('dot');
        localStorage.removeItem('nam');
        history.replace({
          pathname: '/user/login',
        });
        break;

      case 404:
        notification.error({
          message:
            'Lỗi không tìm thấy dữ liệu, bạn hãy thử f5 refresh lại trình duyệt để cập nhật phiên bản mới nhất.',
          description:
            data.error[
              error?.response?.data?.detail?.errorCode || error?.response?.data?.errorCode
            ] ||
            error?.response?.data?.errorDescription ||
            error?.data?.detail?.message ||
            error?.message,
        });
        break;

      case 405:
        notification.error({
          message: 'Truy vấn không được phép',
          description: error?.response?.data?.detail?.message || error?.message,
        });
        break;

      case 409:
        notification.error({
          message: 'Dữ liệu chưa đúng',
          description:
            data.error[
              error?.response?.data?.detail?.errorCode || error?.response?.data?.errorCode
            ] ||
            error?.response?.data?.errorDescription ||
            error?.data?.detail?.message ||
            error?.message,
        });
        break;

      case 500:
        notification.error({
          message: 'Server gặp lỗi',
          description: error?.response?.data?.detail?.message || error.message,
        });
        break;

      default:
        break;
    }
    // Do something with response error
    return Promise.reject(error);
  },
);

export default axios;
