<template>
  <div class="login-container">
    <el-form ref="formRef" :model="form" :rules="rules" class="login-page">
      <h2 class="title" style="margin-bottom: 20px">重置密码</h2>
      <el-form-item prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名" clearable>
          <template #prefix>
            <el-icon class="el-input__icon"><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="phone">
        <el-input v-model="form.phone" placeholder="请输入注册时的手机号码" clearable>
          <template #prefix>
            <el-icon class="el-input__icon"><Phone /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="newPassword">
        <el-input v-model="form.newPassword" placeholder="请输入新密码" clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input v-model="form.confirmPassword" placeholder="请确认新密码" clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="authorize">
        <el-input v-model="form.authorize" placeholder="请输入管理员验证码" clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <div style="display: flex">
          <el-input v-model="form.validCode" style="width: 45%;" placeholder="请输入验证码"></el-input>
          <ValidCode @input="createValidCode" style="width: 50%" />
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" style="width: 100%" @click="resetPassword">重置密码</el-button>
      </el-form-item>
      <el-form-item>
        <a href="javascript:void(0)" class="link-button" @click="$router.push('/login')">返回登录 >></a>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { User, Lock, Phone } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import ValidCode from '../components/ValidCode.vue';
import { resetPasswordAPI } from '@/api/auth.js';

const form = reactive({
  username: '',
  phone: '',
  newPassword: '',
  confirmPassword: '',
  authorize: '',
  validCode: ''
});

const validCode = ref('');
const router = useRouter();
const formRef = ref(null);

const rules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 13, message: '长度要求为2到13位', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' }
  ],
  authorize: [
    { required: true, message: '请输入管理员验证码', trigger: 'blur' }
  ]
});

const createValidCode = (data) => {
  validCode.value = data;
};

const resetPassword = async () => {
  try {
    const valid = await formRef.value.validate();
    if (valid) {
      if (!form.validCode) {
        ElMessage.error("请填写验证码");
        return;
      }
      if (form.validCode.toLowerCase() !== validCode.value.toLowerCase()) {
        ElMessage.error("验证码错误");
        return;
      }
      if (form.newPassword !== form.confirmPassword) {
        ElMessage.error("两次密码输入不一致");
        return;
      }

      const res = await resetPasswordAPI({
        username: form.username,
        phone: form.phone,
        newPassword: form.newPassword,
        authorize: form.authorize
      });

      if (res.data.code === 200) {
        ElMessage.success("密码重置成功");
        router.push("/login");
      } else {
        ElMessage.error(res.data.msg || "重置密码失败");
      }
    }
  } catch (error) {
    console.error(error);
    if (error.response && error.response.data) {
      ElMessage.error(error.response.data.message || "重置密码失败");
    } else {
      ElMessage.error("重置密码失败，请稍后再试");
    }
  }
};
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  width: 100vw;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
  position: relative;
  overflow: hidden;
  animation: fadeInBg 0.8s ease-out;
}
@keyframes fadeInBg {
  from { opacity: 0; }
  to { opacity: 1; }
}
.login-page {
  border-radius: 18px;
  width: 370px;
  padding: 40px 38px 20px;
  background: rgba(255,255,255,0.95);
  border: none;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.18);
  display: flex;
  flex-direction: column;
  align-items: center;
  transition: transform 0.3s cubic-bezier(.25,.8,.25,1), box-shadow 0.3s;
  animation: fadeIn 0.8s ease-out;
}
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}
.login-page:hover {
  transform: translateY(-8px) scale(1.03);
  box-shadow: 0 16px 40px 0 rgba(31, 38, 135, 0.22);
}
.title {
  font-size: 2rem;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
  letter-spacing: 2px;
  text-align: center;
  animation: fadeIn 0.8s ease-out;
}
.el-form-item {
  width: 100%;
}
.el-input, .el-radio-group, .el-button {
  border-radius: 12px !important;
}
.el-input__wrapper {
  background: #f7f8fa;
  border-radius: 12px !important;
  transition: box-shadow 0.2s;
}
.el-input__wrapper:focus-within {
  box-shadow: 0 0 0 2px #fed6e3;
}
.el-button {
  background: #a8edea;
  border: none;
  color: #333;
  font-weight: normal;
  border-radius: 12px;
  transition: background 0.3s, box-shadow 0.3s, transform 0.2s;
  box-shadow: 0 2px 8px rgba(168,237,234,0.15);
}
.el-button:hover {
  background: #fed6e3;
  color: #222;
  transform: translateY(-2px) scale(1.03);
}
.link-button {
  color: #409EFF;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
  cursor: pointer;
  display: inline-block;
  padding: 4px 0;
}
.link-button:hover {
  color: #66b1ff;
  text-decoration: underline;
}
@media (max-width: 500px) {
  .login-page {
    width: 95vw;
    padding: 20px 5vw 10px;
    margin: 0;
  }
}
</style> 