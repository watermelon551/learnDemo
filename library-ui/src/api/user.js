import request from "@/utils/request.js";

export const userInfoAPI = () => {
  return request({
    url: '/api/users',
    method: 'get'
  });
};

export const updateUserAPI = (form) => {
  return request.put(`/api/users/${form.id}`, form)
}

export const getUsersAPI = ({ pageNum, pageSize, name, phone, email }) => {
  return request({
    url: '/api/users/search',
    params: {
      pageNum,
      pageSize,
      name,
      phone,
    },
  });
};

export const deleteUserAPI = (id) => {
  return request.delete(`/api/users/${id}`)
}

export const batchDeleteUsersAPI = (ids) => {
  return request.post('/api/users/deleteBatch', ids)
}
export const updateUserPassword = (data) => {
  return request.post('/api/auth/reset-password', data);
}
