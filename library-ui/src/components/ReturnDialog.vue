<template>
  <div class="dialog-overlay" @click.self="closeDialog">
    <div class="dialog-content">
      <div class="dialog-header">
        <h3>{{ activeTab === 'return' ? '还书操作' : '续借操作' }}</h3>
        <button @click="closeDialog">&times;</button>
      </div>

      <!-- 选项卡 -->
      <div class="tabs">
        <button
            :class="{ active: activeTab === 'return' }"
            @click="activeTab = 'return'"
        >还书</button>
        <button
            :class="{ active: activeTab === 'renew' }"
            @click="activeTab = 'renew'"
        >续借</button>
      </div>

      <!-- 表单 -->
      <div class="form-container">
        <div class="form-group">
          <label>用户名</label>
          <input
              v-model="username"
              type="text"
              placeholder="输入用户名"
          >
        </div>

        <div class="form-group">
          <label>密码</label>
          <input
              v-model="password"
              type="password"
              placeholder="输入密码"
          >
        </div>

        <div class="form-group">
          <label>借阅记录ID</label>
          <input
              v-model="recordId"
              type="number"
              placeholder="输入借阅记录ID"
          >
        </div>

        <div class="form-group" v-if="activeTab === 'renew'">
          <label>续借天数</label>
          <select v-model="renewDays">
            <option value="15">15天</option>
            <option value="30">30天</option>
            <option value="60">60天</option>
          </select>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="dialog-actions">
        <button class="cancel-btn" @click="closeDialog">取消</button>
        <button
            class="confirm-btn"
            @click.stop.prevent="handleSubmit"
            :disabled="isSubmitting"
        >
          {{ activeTab === 'return' ? '确认还书' : '确认续借' }}
        </button>
      </div>

      <!-- 状态提示 -->
      <div v-if="statusMessage" :class="['status-message', statusType]">
        {{ statusMessage }}
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ReturnDialog',
  data() {
    return {
      activeTab: 'return', // 默认选还书
      username: '',
      password: '',
      recordId: null,
      renewDays: '30',
      isSubmitting: false,
      hasSubmitted: false,
      statusMessage: '',
      statusType: '' // success/error
    };
  },
  methods: {
    closeDialog() {
      this.$emit('close');
    },

    handleSubmit() {
      // 防止重复提交
      if (this.isSubmitting || this.hasSubmitted) return;
      this.submitForm();
    },

    async submitForm() {
      this.isSubmitting = true;
      this.hasSubmitted = true;
      this.statusMessage = '';

      try {
        // 准备请求数据
        const request = {
          username: this.username.trim(),
          password: this.password,
          recordId: parseInt(this.recordId),
          type: this.activeTab === 'return' ? '还书' : '续借'
        };

        // 发送请求
        const response = await axios.post(
            'http://localhost:8080/api/books/return',
            request
        );

        // 处理成功响应
        this.statusMessage = response.data;
        this.statusType = 'success';

        // 重置表单
        this.username = '';
        this.password = '';
        this.recordId = null;

        // **立即触发事件，通知父组件刷新**
        this.$emit('return-success');

        // **立即关闭弹窗**
        this.closeDialog();

      } catch (error) {
        // 处理错误
        let errorMessage = error.response?.data || '操作失败';

        // 修正错误消息
        if (error.response?.status === 401) {
          errorMessage = "当前用户名与数据库不匹配，请求失败！";
        }

        this.statusMessage = errorMessage;
        this.statusType = 'error';
      } finally {
        this.isSubmitting = false;
      }
    }

  }
};
</script>

<style scoped>
/* 添加成功/失败提示样式 */
.status-message {
  margin-top: 15px;
  padding: 12px;
  border-radius: 6px;
  text-align: center;
  font-weight: 500;
}

.status-message.success {
  background-color: #dff0d8;
  color: #3c763d;
  border: 1px solid #d6e9c6;
}

.status-message.error {
  background-color: #f2dede;
  color: #a94442;
  border: 1px solid #ebccd1;
}

/* 更新弹窗样式 */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.dialog-content {
  background: white;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  width: 400px;
  max-width: 90%;
  overflow: hidden;
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #3498db;
  color: white;
}

.dialog-header h3 {
  margin: 0;
  font-size: 1.4rem;
}

.dialog-header button {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: white;
  cursor: pointer;
}

.tabs {
  display: flex;
  border-bottom: 1px solid #eee;
}

.tabs button {
  flex: 1;
  padding: 12px;
  border: none;
  background: none;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
}

.tabs button.active {
  position: relative;
  color: #3498db;
}

.tabs button.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 3px;
  background: #3498db;
}

.form-container {
  padding: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
  color: #555;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  padding: 0 20px 20px;
  gap: 10px;
}

.dialog-actions button {
  padding: 10px 20px;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.cancel-btn {
  background: #f8f9fa;
  border: 1px solid #ddd;
  color: #6c757d;
}

.cancel-btn:hover {
  background: #e9ecef;
}

.confirm-btn {
  background: #3498db;
  color: white;
  border: none;
}

.confirm-btn:disabled {
  background: #cbd5e0;
  cursor: not-allowed;
}

.confirm-btn:hover:not(:disabled) {
  background: #2980b9;
}
</style>