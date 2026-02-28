<template>
  <div class="register-member-container">
    <el-card class="page-header">
      <template #header>
        <div class="card-header">
          <h2><el-icon><User /></el-icon> 注册会员</h2>
        </div>
      </template>
      <div class="fee-tip">
        <el-alert title="会员费用：10元/月，按月计费，注册时需预存押金。" type="info" show-icon :closable="false" />
        <el-alert :title="`当前预存款余额：￥${balance}`" type="success" show-icon :closable="false" style="margin-top: 10px;" />
        <el-alert :title="`本次注册需花费：￥${cost}`" type="warning" show-icon :closable="false" style="margin-top: 10px;" />
      </div>
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        v-loading="loading"
      >
        <el-form-item label="用户名">
          <el-input v-model="userInfo.userName" readonly />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="userInfo.phone" readonly />
        </el-form-item>
        <el-form-item label="会员时长" prop="duration">
          <el-select v-model="form.duration" placeholder="请选择会员时长">
            <el-option
              v-for="item in durationOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="loading">注册会员</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { User } from '@element-plus/icons-vue';
import axios from 'axios';
import { useUserStore } from '@/stores/userStore';
import { useRouter } from 'vue-router';
import request from '@/utils/request';

const userStore = useUserStore();
const router = useRouter();
const userInfo = ref({});
const formRef = ref(null);
const loading = ref(false);
const balance = ref(0);

const form = reactive({
  duration: 1
});

const durationOptions = [
  { value: 1, label: '1个月' },
  { value: 3, label: '3个月' },
  { value: 6, label: '6个月' },
  { value: 12, label: '12个月' }
];

const rules = {
  duration: [
    { required: true, message: '请选择会员时长', trigger: 'change' }
  ]
};

const cost = computed(() => form.duration * 10);

const loadUserInfo = async () => {
  try {
    await userStore.getUserInfo();
    userInfo.value = userStore.userInfo;
    // 查询预存款余额
    const res = await request.get(`/api/financial/prepayment/balance/${userInfo.value.userName}`);
    balance.value = res.data.data;
  } catch (error) {
    ElMessage.error('获取用户信息或余额失败');
  }
};

const submitForm = async () => {
  if (!formRef.value) return;

  try {
    await formRef.value.validate();
    loading.value = true;

    await request.post('/api/users/members', { ...form, username: userInfo.value.userName });
    ElMessage.success('注册会员成功！');
    router.push('/member-center');
  } catch (error) {
    console.error('注册失败:', error);
    ElMessage.error(error.response?.data?.message || '注册失败，请重试');
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  loadUserInfo();
});
</script>

<style scoped>
.register-member-container {
  padding: 24px;
  background: #f5f7fa;
  min-height: 100vh;
}
.page-header {
  margin-bottom: 24px;
  max-width: 500px;
  margin-left: auto;
  margin-right: auto;
}
.card-header {
  display: flex;
  align-items: center;
  gap: 10px;
}
.card-header h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #303133;
  font-weight: 600;
}
.fee-tip {
  margin-bottom: 18px;
}
.register-form {
  max-width: 400px;
  margin: 0 auto;
}
</style>
