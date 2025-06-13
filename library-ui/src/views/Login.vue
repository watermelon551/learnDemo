<template>
  <div class="login-container">
    <el-form ref="formRef" :model="form" :rules="rules" class="login-page">
      <h2 class="title" style="margin-bottom: 20px">系统登陆</h2>
      <el-form-item prop="username">
        <el-input v-model="form.username" clearable>
          <template #prefix>
            <el-icon class="el-input__icon"><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password" clearable show-password>
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
        <div class="mb-2 flex items-center text-sm">
          <el-radio-group  v-model="form.rule" class="ml-4">
            <el-radio value="1" size="default">管理员</el-radio>
            <el-radio value="2" size="default">读者</el-radio>
          </el-radio-group>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" style="width: 100%" @click="login">登 录</el-button>
      </el-form-item>
      <el-form-item style="display: flex; justify-content: space-between;">
        <a href="javascript:void(0)" class="link-button" @click="$router.push('/register')">前往注册 >></a>
        <!-- 忘记密码 放在最右侧-->
        <div style="margin-left: auto;">
          <a href="javascript:void(0)" class="link-button" @click="$router.push('/reset-password')">忘记密码？</a>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { User, Lock } from '@element-plus/icons-vue';
import { useUserStore } from '@/stores/userStore.js';
import ValidCode from '../components/ValidCode.vue';
import { loginAPI } from '@/api/auth.js';

const form = ref({
  username: '',
  password: '',
  validCode: ''
});

const validCode = ref('');

const rules = ref({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
});

const router = useRouter();
const formRef = ref(null);

const createValidCode = (data) => {
  validCode.value = data;
};

const login = async () => {
  try {
    const valid = await formRef.value.validate();
    if (valid) {
      if (!form.value.validCode) {
        ElMessage.error("请填写验证码");
        return;
      }
      if (form.value.validCode.toLowerCase() !== validCode.value.toLowerCase()) {
        ElMessage.error("验证码错误");
        return;
      }

      const res = await loginAPI(form.value);
      if (res.data.code === 200) {
        ElMessage.success("登录成功");
        localStorage.setItem('token', res.data.token)
        await useUserStore().getUserInfo();
        router.push("/book");
      } else {
        ElMessage.error(res.data.message);
      }
    }
  } catch (error) {
    console.error(error);
    ElMessage.error("登录失败，请稍后再试");
  }
};

</script>

<style scoped>
.login-container {
  position: fixed;
  width: 100vw;
  height: 100vh;
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
  display: flex;
  align-items: center;
  justify-content: center;
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
</style>
