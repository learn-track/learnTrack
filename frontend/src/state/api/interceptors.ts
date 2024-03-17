import axios, { AxiosError } from 'axios';

axios.interceptors.response.use(
  (response) => {
    return response;
  },
  (error: AxiosError) => {
    if (error.response && error.response.status === 401 && error.response.data === 'Invalid Token') {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }

    return error;
  },
);

axios.interceptors.request.use((request) => {
  const token = localStorage.getItem('token');
  if (token) {
    request.headers.Authorization = `Bearer ${token}`;
  }
  return request;
});
