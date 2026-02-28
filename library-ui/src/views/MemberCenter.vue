<template>
  <div class="member-center-container">
    <el-card class="member-card">
      <template #header>
        <div class="header-title">
          <el-icon><User /></el-icon>
          <span>会员中心</span>
        </div>
      </template>
      <div v-if="member">
        <el-descriptions title="我的会员权益" :column="1" border>
          <el-descriptions-item label="最大可借书数">{{ member.maxBooks }}</el-descriptions-item>
          <el-descriptions-item label="免费租赁天数">{{ member.freeDays }}</el-descriptions-item>
          <el-descriptions-item label="押金">￥{{ member.deposit }}</el-descriptions-item>
          <el-descriptions-item label="租金">￥{{ member.rentPay }}</el-descriptions-item>
          <el-descriptions-item label="滞纳金">￥{{ member.overdueFine }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <div v-else>
        <el-empty description="您还不是会员，请先注册会员" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElDescriptions, ElDescriptionsItem, ElEmpty } from 'element-plus';
import { User } from '@element-plus/icons-vue';
import request from '@/utils/request';
import { useUserStore } from '@/stores/userStore';

const userStore = useUserStore();
const member = ref(null);

const loadMemberInfo = async () => {
  try {
    await userStore.getUserInfo();
    const userId = userStore.userInfo.id;
    const res = await request.get('/api/users/members');
    // 假设后端返回所有会员，前端筛选当前用户
    if (res.data && res.data.data) {
      member.value = res.data.data.find(m => m.userId === userId) || null;
    }
  } catch (e) {
    ElMessage.error('获取会员信息失败');
  }
};

onMounted(() => {
  loadMemberInfo();
});
</script>

<style scoped>
.member-center-container {
  padding: 24px;
  background: #f5f7fa;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}
.member-card {
  width: 500px;
}
.header-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 1.3rem;
  font-weight: 600;
}
</style> 