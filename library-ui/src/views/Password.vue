<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>修改密码</span>
        </div>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">

        <el-form-item label="管理员密码" prop="adminPassword">
          <el-input v-model="form.adminPassword" type="password" placeholder="请输入管理员密码" show-password>
            <template #prefix>
              <el-icon class="el-input__icon"><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="当前密码" prop="currentPassword">
          <el-input v-model="form.currentPassword" type="password" placeholder="请输入当前密码" show-password>
            <template #prefix>
              <el-icon class="el-input__icon"><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="form.newPassword" type="password" placeholder="请输入新密码" show-password>
            <template #prefix>
              <el-icon class="el-input__icon"><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="请确认新密码" show-password>
            <template #prefix>
              <el-icon class="el-input__icon"><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="submitForm">保存</el-button>
          <el-button @click="$router.push('/person')">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { Lock, Phone } from '@element-plus/icons-vue';
import { useUserStore } from '@/stores/userStore.js';
import { updateUserPassword } from '@/api/user.js';

const router = useRouter();
const userStore = useUserStore();
const formRef = ref(null);
const loading = ref(false);

const form = reactive({
  phone: '',
  adminPassword: '',
  currentPassword: '',
  newPassword: '',
  confirmPassword: '',
});

const validatePhone = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入手机号'));
  } else if (!/^1[3-9]\d{9}$/.test(value)) {
    callback(new Error('请输入正确的手机号'));
  } else if (value !== userStore.userInfo.phone) {
    callback(new Error('手机号与账号不匹配'));
  } else {
    callback();
  }
};

const validateAdminPassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入管理员验证码'));
  } else if (value !== '1234') {
    callback(new Error('管理员验证码错误'));
  } else {
    callback();
  }
};

const validateNewPassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入新密码'));
  } else if (value.length < 6) {
    callback(new Error('密码长度不能小于6位'));
  } else if (value === form.currentPassword) {
    callback(new Error('新密码不能与当前密码相同'));
  } else {
    callback();
  }
};

const rules = reactive({
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { validator: validatePhone, trigger: 'blur' }
  ],
  adminPassword: [
    { required: true, message: '请输入管理员验证码', trigger: 'blur' },
    { validator: validateAdminPassword, trigger: 'blur' }
  ],
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { validator: validateNewPassword, trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请确认新密码'));
        } else if (value !== form.newPassword) {
          callback(new Error('两次输入密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur',
    },
  ],
});

onMounted(() => {
  form.phone = userStore.userInfo.phone || '';
});

const submitForm = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true;
      try {
        const res = await updateUserPassword({
          username: userStore.userInfo.userName,
          oldPassword: form.currentPassword,
          newPassword: form.newPassword,
          authorize: form.adminPassword,
          phone: form.phone
        });
        if (res.data.code === 200) {
          ElMessage.success('密码修改成功，请重新登录');
          userStore.clearUserInfo();
          localStorage.removeItem('token');
          router.push('/login');
        } else {
          ElMessage.error(res.data.message || '密码修改失败');
        }
      } catch (error) {
        ElMessage.error(error?.response?.data?.message || '密码修改失败');
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
