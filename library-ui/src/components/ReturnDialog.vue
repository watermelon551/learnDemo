<template>
  <div class="dialog-overlay" @click.self="closeDialog">
    <div class="dialog-content">
      <div class="dialog-header">
        <h3>还书操作</h3>
        <button @click="closeDialog">&times;</button>
      </div>
      <div class="form-container">
        <div v-if="borrowList && borrowList.length > 0">
          <label>请选择要归还的书籍：</label>
          <el-table :data="borrowList" border stripe style="margin: 12px 0;" @selection-change="onSelectionChange">
            <el-table-column type="selection" width="50" />
            <el-table-column prop="bookTitle" label="书名" />
            <el-table-column prop="author" label="作者" />
            <el-table-column prop="borrowDate" label="借阅日期" />
            <el-table-column prop="dueDate" label="应还日期" />
          </el-table>
        </div>
        <div v-else class="no-borrow">暂无可归还的书籍</div>
      </div>
      <div class="dialog-actions">
        <button class="cancel-btn" @click="closeDialog">取消</button>
        <button
            class="confirm-btn"
          @click.stop.prevent="handleReturn"
          :disabled="selected.length === 0 || isSubmitting"
        >确认还书</button>
      </div>
      <div v-if="statusMessage" :class="['status-message', statusType]">
        {{ statusMessage }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, defineProps, defineEmits } from 'vue';
import request from '@/utils/request.js';
const props = defineProps({
  borrowList: { type: Array, default: () => [] }
});
const emit = defineEmits(['close', 'return-success']);
const selected = ref([]);
const isSubmitting = ref(false);
const statusMessage = ref('');
const statusType = ref('');

watch(props.borrowList, () => { selected.value = []; });

function closeDialog() {
  emit('close');
}

function onSelectionChange(val) {
  selected.value = val;
}

async function handleReturn() {
  if (selected.value.length === 0) return;
  isSubmitting.value = true;
  statusMessage.value = '';
      try {
    // 支持多选归还
    for (const item of selected.value) {
      const recordId = item.id || item.borrowId;
      await request.post(`/api/borrow/return/${recordId}`);
    }
    statusMessage.value = '还书成功';
    statusType.value = 'success';
    setTimeout(() => {
      emit('return-success');
      closeDialog();
    }, 1000);
  } catch (e) {
    statusMessage.value = '还书失败';
    statusType.value = 'error';
      } finally {
    isSubmitting.value = false;
      }
    }
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

.no-borrow {
  text-align: center;
  color: #888;
  padding: 2rem 0;
}
</style>