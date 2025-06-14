<template>
  <div class="login-container">
    <el-form ref="formRef" :model="form" :rules="rules" class="login-page">
      <h2 class="title" style="margin-bottom: 20px">用户注册</h2>
      <el-form-item prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名" clearable>
          <template #prefix>
            <el-icon class="el-input__icon"><User/></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="nickName">
        <el-input v-model="form.nickName" placeholder="请输入昵称" clearable>
          <template #prefix>
            <el-icon class="el-input__icon"><User/></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="phone">
        <el-input v-model="form.phone" placeholder="请输入手机号码" clearable>
          <template #prefix>
            <el-icon class="el-input__icon"><Phone /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password" placeholder="请输入密码" clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock/></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirm">
        <el-input v-model="form.confirm" placeholder="请再次确认密码" clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock/></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="rule" class="role-form-item">
        <div class="role-selector">
          <div class="role-label">选择您的身份</div>
          <el-radio-group v-model="form.rule" class="role-group">
            <el-radio-button value="1">
              <el-icon><UserFilled /></el-icon>
              <span>管理员</span>
            </el-radio-button>
            <el-radio-button value="2">
              <el-icon><Reading /></el-icon>
              <span>读者</span>
            </el-radio-button>
          </el-radio-group>
        </div>
      </el-form-item>
      <el-form-item prop="authorize" v-if="form.rule==='1'">
        <el-input v-model="form.authorize" placeholder="请输入管理员注册码" clearable show-password>
          <template #prefix>
            <el-icon class="el-input__icon"><Lock/></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item>
        <div class="valid-code">
          <el-input v-model="form.validCode" placeholder="请输入验证码" style="width: 100%"></el-input>
          <ValidCode @input="createValidCode" style="width: 120px; margin-left: 12px;"/>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" style="width: 100%" @click="register">注 册</el-button>
      </el-form-item>
      <el-form-item>
        <a href="javascript:void(0)" class="link-button" @click="$router.push('/login')">前往登录 >></a>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import ValidCode from "../components/ValidCode.vue";
import { User, Lock, UserFilled, Reading, Phone } from '@element-plus/icons-vue';
import {useRouter} from "vue-router";
import { registerAPI } from '@/api/auth.js';

const form = reactive({
  username: '',
  password: '',
  confirm: '',
  rule: '2',
  authorize: '',
  validCode: '',
  nickName: '',
  phone: '',
});

const router = useRouter()
const validCode = ref('');

const rules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 13, message: '长度要求为2到13位', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  confirm: [
    { required: true, message: '请确认密码', trigger: 'blur' }
  ],
  authorize: [
    { required: true, message: '请输入注册码', trigger: 'blur' }
  ]
});

const createValidCode = (data) => {
  validCode.value = data;
};

const register = async () => {
  try {
    if (!form.validCode) {
      ElMessage.error("请填写验证码");
      return;
    }
    if (form.validCode.toLowerCase() !== validCode.value.toLowerCase()) {
      ElMessage.error("验证码错误");
      return;
    }
    if (form.password !== form.confirm) {
      ElMessage.error("两次密码输入不一致");
      return;
    }
    if (form.rule === '1' && form.authorize !== "1234") {
      ElMessage.error("请输入正确的注册码");
      return;
    }
    
    // 构建注册数据
    const registerData = {
      username: form.username,
      password: form.password,
      nickName: form.nickName,
      phone: form.phone,
      role: form.rule === '1' ? 'ADMIN' : 'USER'  // 根据选择的角色设置对应的值
    };
    
    const res = await registerAPI(registerData);
    if (res.data.code === 200) {
      ElMessage.success("注册成功");
      router.push("/login");
    } else {
      ElMessage.error(res.data.message || "注册失败");
    }
  } catch (error) {
    console.error('注册错误:', error);
    if (error.code === 'ERR_NETWORK') {
      ElMessage.error("网络连接失败，请检查后端服务是否正常运行");
    } else if (error.response) {
      // 服务器返回了错误状态码
      ElMessage.error(`服务器错误: ${error.response.status} - ${error.response.data?.message || '未知错误'}`);
    } else {
      ElMessage.error("注册失败，请稍后重试");
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
@keyframes fadeInTitle {
  from { opacity: 0; }
  to { opacity: 1; }
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
.el-form-item__content {
  display: flex;
  align-items: center;
}
@media (max-width: 500px) {
  .login-page {
    width: 95vw;
    padding: 20px 5vw 10px;
    margin: 0;
  }
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
.role-form-item {
  margin: 15px 0;
}

.role-selector {
  width: 100%;
}

.role-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 12px;
  font-weight: 500;
}

.role-group {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.role-group :deep(.el-radio-button__inner) {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  border-radius: 20px !important;
  transition: all 0.3s ease;
}

.role-group :deep(.el-radio-button__inner:hover) {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.role-group :deep(.el-radio-button__original-radio:checked + .el-radio-button__inner) {
  background-color: #409EFF;
  border-color: #409EFF;
  box-shadow: 0 2px 12px rgba(64, 158, 255, 0.2);
}

.role-group :deep(.el-radio-button__inner .el-icon) {
  font-size: 16px;
  margin-right: 4px;
}

.role-group :deep(.el-radio-button:first-child .el-radio-button__inner) {
  border-radius: 20px !important;
}

.role-group :deep(.el-radio-button:last-child .el-radio-button__inner) {
  border-radius: 20px !important;
}

.el-input {
  --el-input-hover-border-color: #a8edea;
  --el-input-focus-border-color: #409EFF;
  --el-input-border-radius: 12px;
  --el-input-bg-color: #f0f2f5;
  --el-input-border-color: #cccfd3;
  --el-input-hover-bg-color: #ffffff;
  --el-input-focus-bg-color: #ffffff;
  --el-input-text-color: #334155;
  --el-input-placeholder-color: #94a3b8;
  --el-input-icon-color: #64748b;
  --el-input-clear-hover-color: #409EFF;
  --el-input-padding-horizontal: 16px;
  margin: 4px 0;
}

.el-input :deep(.el-input__wrapper) {
  padding: 8px 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.02);
  transition: all 0.3s ease;
}

.el-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transform: translateY(-1px);
  border-color: #a8edea;
}

.el-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
  border-color: #409EFF;
  transform: translateY(-1px);
}

.el-input :deep(.el-input__inner) {
  height: 42px;
  font-size: 15px;
  letter-spacing: 0.3px;
}

.el-input :deep(.el-input__prefix) {
  font-size: 18px;
  margin-right: 8px;
  color: #94a3b8;
  transition: color 0.3s ease;
}

.el-input :deep(.el-input__wrapper.is-focus .el-input__prefix) {
  color: #409EFF;
}

.el-input :deep(.el-input__suffix) {
  font-size: 18px;
  color: #94a3b8;
  transition: color 0.3s ease;
}

.el-input :deep(.el-input__wrapper.is-focus .el-input__suffix) {
  color: #409EFF;
}

.el-input :deep(.el-input__clear) {
  font-size: 16px;
  color: #94a3b8;
  transition: all 0.3s ease;
}

.el-input :deep(.el-input__clear:hover) {
  color: #409EFF;
  transform: scale(1.1);
}

/* 验证码输入框特殊样式 */
.valid-code {
  display: flex;
  align-items: center;
  width: 100%;
}

.valid-code .el-input {
  flex: 1;
}

.valid-code :deep(.el-input__wrapper) {
  height: 42px;
}

/* 密码输入框特殊样式 */
.el-input[type="password"] :deep(.el-input__wrapper) {
  padding-right: 40px;
}

/* 输入框聚焦时的动画效果 */
@keyframes inputFocus {
  0% { transform: translateY(0); box-shadow: 0 2px 4px rgba(0, 0, 0, 0.02); }
  50% { transform: translateY(-2px); box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.2); }
  100% { transform: translateY(-1px); box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2); }
}

.el-input :deep(.el-input__wrapper.is-focus) {
  animation: inputFocus 0.3s ease;
}

/* 响应式调整 */
@media (max-width: 500px) {
  .el-input {
    --el-input-padding-horizontal: 12px;
  }
  
  .el-input :deep(.el-input__inner) {
    height: 38px;
    font-size: 14px;
  }
  
  .el-input :deep(.el-input__prefix) {
    font-size: 16px;
  }
}
</style>
