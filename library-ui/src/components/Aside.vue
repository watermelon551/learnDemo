<template>
  <div class="aside-container">
    <el-menu
      :default-active="path"
      router
      class="el-menu-vertical"
      :collapse="isCollapse"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409EFF"
    >
      <el-menu-item index="/dashboard" v-if="user.role === 'ADMIN'" class="menu-item">
        <el-icon><DataLine /></el-icon>
        <template #title>
        <span>数据统计</span>
        </template>
      </el-menu-item>

      <el-menu-item index="/admin-books" v-if="user.role === 'ADMIN'" class="menu-item">
        <el-icon><Reading /></el-icon>
        <template #title>
        <span>书籍管理</span>
        </template>
      </el-menu-item>

      <el-menu-item index="/books" v-if="user.role === 'USER'" class="menu-item">
        <el-icon><Search /></el-icon>
        <template #title>
        <span>图书查询</span>
        </template>
      </el-menu-item>

      <el-menu-item index="/user" v-if="user.role === 'ADMIN'" class="menu-item">
        <el-icon><User /></el-icon>
        <template #title>
        <span>用户管理</span>
        </template>
      </el-menu-item>

      <el-menu-item index="/lendrecord" v-if="user.role === 'ADMIN'" class="menu-item">
        <el-icon><List /></el-icon>
        <template #title>
        <span>借阅管理</span>
        </template>
      </el-menu-item>

      <el-menu-item index="/bookwithuser" v-if="user.role === 'USER'" class="menu-item">
        <el-icon><Grid /></el-icon>
        <template #title>
        <span>借阅查询</span>
        </template>
      </el-menu-item>

      <el-sub-menu index="profile" class="menu-item">
        <template #title>
          <el-icon><UserFilled /></el-icon>
          <span>个人信息</span>
        </template>
        <el-menu-item index="/person" class="sub-menu-item">
          <el-icon><Edit /></el-icon>
          <span>修改个人信息</span>
        </el-menu-item>
        <el-menu-item index="/password" class="sub-menu-item">
          <el-icon><Lock /></el-icon>
          <span>修改密码</span>
        </el-menu-item>
      </el-sub-menu>
    </el-menu>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRoute } from 'vue-router';
import { useUserStore } from '@/stores/userStore.js';
import {
  DataLine,
  Reading,
  Search,
  User,
  List,
  Grid,
  UserFilled,
  Edit,
  Lock
} from '@element-plus/icons-vue';

const user = ref({});
const route = useRoute();
const path = computed(() => route.path);
const userStore = useUserStore();
const isCollapse = ref(false);

user.value = userStore.userInfo;

// 监听窗口大小变化
const handleResize = () => {
  isCollapse.value = window.innerWidth <= 768;
};

onMounted(() => {
  handleResize();
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
});
</script>

<style scoped>
.aside-container {
  height: 100%;
  background-color: #304156;
  transition: width 0.3s ease;
}

.el-menu-vertical {
  border-right: none;
  height: 100%;
}

.el-menu-vertical:not(.el-menu--collapse) {
  width: 200px;
}

.menu-item {
  transition: all 0.3s ease;
}

.menu-item:hover {
  background-color: #263445 !important;
}

.sub-menu-item {
  padding-left: 48px !important;
}

.el-menu-item.is-active {
  background-color: #263445 !important;
}

.el-menu-item.is-active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background-color: #409EFF;
}

.el-icon {
  font-size: 18px;
  vertical-align: middle;
  margin-right: 8px;
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .aside-container {
    position: fixed;
    left: 0;
    top: 60px;
    bottom: 0;
    z-index: 999;
    transform: translateX(-200px);
    transition: transform 0.3s ease;
  }

  .aside-container.show {
    transform: translateX(0);
  }
}
</style>
