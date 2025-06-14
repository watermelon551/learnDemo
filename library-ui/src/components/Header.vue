<template>
  <div class="header">
    <div class="header-left">
    <div class="logo">
      <img src="../assets/icon/stack-of-books.png" class="icon" alt="" />
        <span class="title">智慧图书交互终端</span>
      </div>
    </div>
    <div class="header-right">
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="user-info">
          <el-avatar :size="32" class="user-avatar">
            {{ user.nickName?.charAt(0)?.toUpperCase() }}
          </el-avatar>
          <span class="user-name">{{ user.nickName }}</span>
          <el-icon class="el-icon--right">
            <arrow-down />
          </el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>个人信息
            </el-dropdown-item>
            <el-dropdown-item command="password">
              <el-icon><Lock /></el-icon>修改密码
            </el-dropdown-item>
            <el-dropdown-item divided command="exit">
              <el-icon><SwitchButton /></el-icon>退出系统
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { ArrowDown, User, Lock, SwitchButton } from '@element-plus/icons-vue';
import { useUserStore } from '@/stores/userStore.js';

const user = ref({});
const router = useRouter();
const userStore = useUserStore();
user.value = userStore.userInfo;

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/person');
      break;
    case 'password':
      router.push('/password');
      break;
    case 'exit':
      exit();
      break;
  }
};

const exit = () => {
  userStore.clearUserInfo();
  router.push('/login');
  ElMessage.success('退出系统成功');
};
</script>

<style scoped>
.header {
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background: linear-gradient(to right, #ffffff, #f8f9fa);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.logo:hover {
  transform: scale(1.02);
}

.icon {
  width: 36px;
  height: 36px;
  margin-right: 12px;
  transition: transform 0.3s ease;
}

.title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #2c3e50;
  letter-spacing: 0.5px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  padding: 5px 10px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.user-info:hover {
  background-color: rgba(0, 0, 0, 0.04);
}

.user-avatar {
  background-color: #409EFF;
  color: #fff;
  font-weight: 500;
  margin-right: 8px;
}

.user-name {
  font-size: 14px;
  color: #606266;
  margin-right: 4px;
}

.el-dropdown-menu__item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
}

.el-dropdown-menu__item .el-icon {
  margin-right: 4px;
}

@media screen and (max-width: 768px) {
  .title {
    display: none;
  }

  .user-name {
    display: none;
  }

  .header {
    padding: 0 10px;
  }
}
</style>
