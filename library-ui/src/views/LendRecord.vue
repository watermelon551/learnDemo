<template>
  <div class="lend-record-container">
    <el-card class="page-header" :body-style="{ display: 'none' }">
      <template #header>
        <div class="card-header">
          <h2><el-icon><List /></el-icon> 借阅管理</h2>
        </div>
      </template>
    </el-card>
    <el-table :data="records" stripe border style="margin-top: 20px;">
      <el-table-column prop="id" label="编号" width="80" align="center" />
      <el-table-column prop="userId" label="用户ID" width="100" align="center" />
      <el-table-column prop="bookId" label="图书ID" width="100" align="center" />
      <el-table-column prop="borrowDate" label="借出日期" width="120" align="center" />
      <el-table-column prop="dueDate" label="应还日期" width="120" align="center" />
      <el-table-column prop="returnDate" label="归还日期" width="120" align="center" />
      <el-table-column prop="status" label="状态" width="100" align="center" />
      <el-table-column label="操作" width="180" align="center">
        <template #default="{ row }">
          <el-button size="small" type="success" @click="handleReturn(row)" :disabled="row.status==='已归还'">还书</el-button>
          <el-button size="small" type="primary" @click="handleRenew(row)" :disabled="row.status==='已归还'">续借</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { List } from '@element-plus/icons-vue';
import request from '../utils/request';

const records = ref([]);

const loadRecords = async () => {
  try {
    const res = await request.get('/api/borrow/records');
    records.value = res.data.data;
  } catch (e) {
    ElMessage.error('加载借阅记录失败');
  }
};

const handleReturn = async (row) => {
  try {
    await request.post(`/api/borrow/return/${row.id}`);
    ElMessage.success('还书成功');
    loadRecords();
  } catch (e) {
    ElMessage.error('还书失败');
  }
};

const handleRenew = async (row) => {
  try {
    await request.post(`/api/borrow/renew/${row.id}`);
    ElMessage.success('续借成功');
    loadRecords();
  } catch (e) {
    ElMessage.error('续借失败');
  }
};

onMounted(() => {
  loadRecords();
});
</script>

<style scoped>
.lend-record-container {
  padding: 24px;
  background: #f5f7fa;
  min-height: 100vh;
}
.page-header {
  margin-bottom: 24px;
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
</style>
