import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { userInfoAPI } from '@/api/user.js';

// 使用 setup 语法定义 store
export const useUserStore = defineStore('user', () => {
  // state
  const userInfo = ref({});

  // getters
  const isAdmin = computed(() => {
    return userInfo.value?.role === 'ADMIN';
  });

  const isUser = computed(() => {
    return userInfo.value?.role === 'USER';
  });

  // actions
  async function getUserInfo() {
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        throw new Error('No token found');
      }
    const res = await userInfoAPI(token);
      if (res.data.code === 200) {
    userInfo.value = res.data.data;
        // 根据角色设置路由权限
        if (userInfo.value.role === 'ADMIN') {
          localStorage.setItem('userRole', 'ADMIN');
        } else {
          localStorage.setItem('userRole', 'USER');
        }
      } else {
        throw new Error(res.data.message || '获取用户信息失败');
      }
    } catch (error) {
      console.error('获取用户信息失败:', error);
      throw error;
    }
  }

  function clearUserInfo() {
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
    userInfo.value = {};
  }

  // 返回 state、getters 和 actions
  return {
    userInfo,
    isAdmin,
    isUser,
    getUserInfo,
    clearUserInfo
  };
}, {
  persist: {
    key: 'user-store',
    storage: localStorage,
    paths: ['userInfo']
  }
});
