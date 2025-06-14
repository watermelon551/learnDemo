import request from "@/utils/request.js";

export const getAllBooks = () => {
  return request({
    url: '/api/books',
    method: 'get'
  });
};

export const searchBooks = (keyword) => {
  return request({
    url: '/api/books/search',
    method: 'get',
    params: { keyword }
  });
};

export const borrowBook = (bookId, data) => {
  return request({
    url: `/api/books/borrow/${bookId}`,
    method: 'post',
    data
  });
};

export const returnBook = (bookId, data) => {
  return request({
    url: `/api/books/return/${bookId}`,
    method: 'post',
    data
  });
};

export const addBook = (data) => {
  return request({
    url: '/api/books/add',
    method: 'post',
    data
  });
};

export const updateBook = (bookId, data) => {
  return request({
    url: `/api/books/${bookId}`,
    method: 'put',
    data
  });
};

export const deleteBook = (bookId, adminPwd) => {
  return request({
    url: `/api/books/${bookId}`,
    method: 'delete',
    data: adminPwd
  });
};
