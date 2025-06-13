import request from "@/utils/request"

export const loginAPI = (form) => {
  return request.post('/api/auth/login', form)
}

export const registerAPI = (form) => {
  return request.post('/api/auth/register', form)
}

// 重置密码
export function resetPasswordAPI(data) {
    return request({
        url: '/api/auth/reset-password',
        method: 'post',
        data
    });
}
