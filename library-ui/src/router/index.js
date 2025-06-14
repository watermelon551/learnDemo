import { createRouter, createWebHistory } from 'vue-router'
import ResetPassword from '../views/ResetPassword.vue'
import Layout from "@/layout/Layout.vue";
import { useUserStore } from '@/stores/userStore';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'layout',
      component: Layout,
      children: [
        {
          path: 'user',
          name: 'user',
          component:()=>import('@/views/User.vue')
        },
        {
          path: 'person',
          name: 'person',
          component: () => import('@/views/Person.vue'),
        },
        {
          path: 'password',
          name: 'password',
          component: () => import('@/views/Password.vue'),
        },
        {
          path: 'books',
          name: 'BookSearch',
          component: () => import('@/views/BookSearch.vue')
        },
        {
          path: 'admin-books',
          name: 'AdminBookView',
          component: () => import('@/views/AdminBookView.vue'),
          meta: { requiresAdmin: true }
        },
        {
          path: 'financial',
          name: 'Financial',
          component: () => import('@/views/Financial.vue'),
          meta: { requiresAdmin: true }
        }
      ],
      meta: { requiresAuth: true }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/Register.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/Login.vue'),
    },
    {
      path: '/reset-password',
      name: 'ResetPassword',
      component: ResetPassword
    }
  ],
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem('token');
  const userRole = localStorage.getItem('userRole');
  const userStore = useUserStore();

  // 需要登录的页面
  if (to.meta.requiresAuth) {
    if (!token) {
      next('/login');
      return;
    }

    // 如果没有用户信息，尝试获取
    if (!userStore.userInfo?.id) {
      try {
        await userStore.getUserInfo();
      } catch (error) {
        next('/login');
        return;
      }
    }

    // 检查管理员权限
    if (to.meta.requiresAdmin && userRole !== 'ADMIN') {
      next('/');
      return;
    }
  }

  // 已登录用户不能访问登录和注册页
  if (token && (to.path === '/login' || to.path === '/register')) {
    next('/');
    return;
  }

  next();
});

export default router
