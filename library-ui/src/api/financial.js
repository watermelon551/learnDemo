import request from "@/utils/request.js";

// 押金操作
export const handleDeposit = (params) => {
  return request({
    url: '/api/financial/deposit',
    method: 'post',
    params: {
      ...params,
      username: params.username
    }
  });
};

// 预存款操作
export const handlePrepayment = (params) => {
  return request({
    url: '/api/financial/prepayment',
    method: 'post',
    params: {
      ...params,
      username: params.username
    }
  });
};

// 罚款操作
export const handleFine = (params) => {
  return request({
    url: '/api/financial/fine',
    method: 'post',
    params: {
      ...params,
      username: params.username
    }
  });
};

// 按用户名查询记录
export const getRecordsByUser = (username) => {
  return request({
    url: `/api/financial/user/${username}`,
    method: 'get'
  });
};

// 按类型查询记录
export const getRecordsByType = (type) => {
  return request({
    url: `/api/financial/type/${type}`,
    method: 'get'
  });
};
