<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
        </div>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="form.nickName" placeholder="请输入昵称"></el-input>
        </el-form-item>
        <el-form-item label="权限">
          <el-input v-model="form.role" disabled></el-input>
        </el-form-item>
        <el-form-item label="电话号码" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话号码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="update">保存</el-button>
          <el-button @click="$router.push('/password')">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useUserStore } from '@/stores/userStore.js';
import { updateUserAPI } from '@/api/user.js';

const userStore = useUserStore();
const formRef = ref(null);
const loading = ref(false);

const form = reactive({
  id: '',
  username: '',
  nickName: '',
  role: '',
  phone: '',
});

const rules = reactive({
  nickName: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入电话号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
});

onMounted(async () => {
  try {
    await userStore.getUserInfo();
    if (userStore.userInfo) {
      form.id = userStore.userInfo.id;
      form.username = userStore.userInfo.userName;
      form.nickName = userStore.userInfo.nickName;
      form.role = userStore.userInfo.role;
      form.phone = userStore.userInfo.phone;
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败');
  }
});

const update = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        const res = await updateUserAPI(form);
        if (res.data.code === 200) {
          ElMessage.success('更新用户信息成功');
          await userStore.getUserInfo();
        } else {
          ElMessage.error(res.data.message || '更新失败');
        }
      } catch (error) {
        ElMessage.error(error?.response?.data?.message || '更新失败');
      } finally {
        loading.value = false;
      }
    }
  });
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.box-card {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-form {
  margin-top: 20px;
}

.el-button {
  margin-right: 10px;
}
</style>
