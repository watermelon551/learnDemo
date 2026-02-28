<template>
  <div class="member-manage-bg">
  <div class="member-manage-container">
      <el-card class="page-header" :body-style="{ display: 'none' }">
      <template #header>
        <div class="card-header">
          <h2><el-icon><User /></el-icon> 会员管理</h2>
        </div>
      </template>
    </el-card>
      <el-table :data="members" stripe border style="min-width: 900px; width: 100%;">
      <el-table-column prop="memberId" label="会员ID" width="80" align="center" />
      <el-table-column prop="userId" label="用户ID" width="100" align="center" />
        <el-table-column label="用户名" width="140" align="center">
          <template #default="{ row }">
            {{ userMap[row.userId] || '-' }}
          </template>
        </el-table-column>
      <el-table-column prop="maxBooks" label="可借书数" width="100" align="center" />
      <el-table-column prop="freeDays" label="免费天数" width="100" align="center" />
      <el-table-column prop="deposit" label="押金" width="100" align="center" />
      <el-table-column prop="rentPay" label="租金" width="100" align="center" />
      <el-table-column prop="overdueFine" label="滞纳金" width="100" align="center" />
      <el-table-column label="操作" width="260" align="center">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="editMember(row)">编辑</el-button>
          <el-button size="small" type="success" @click="recharge(row)">充值</el-button>
          <el-button size="small" type="warning" @click="freeze(row)" :disabled="row.status==='冻结'">冻结</el-button>
          <el-button size="small" @click="viewLogs(row)">日志</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="editDialogVisible" title="编辑会员" width="400px">
      <el-form :model="editForm">
        <el-form-item label="可借书数"><el-input v-model.number="editForm.maxBooks" /></el-form-item>
        <el-form-item label="免费天数"><el-input v-model.number="editForm.freeDays" /></el-form-item>
        <el-form-item label="有效期"><el-date-picker v-model="editForm.validUntil" type="date" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible=false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </template>
    </el-dialog>
    <el-dialog v-model="rechargeDialogVisible" title="会员充值" width="300px">
      <el-input v-model.number="rechargeAmount" placeholder="请输入充值金额" />
      <template #footer>
        <el-button @click="rechargeDialogVisible=false">取消</el-button>
        <el-button type="primary" @click="submitRecharge">充值</el-button>
      </template>
    </el-dialog>
    <el-dialog v-model="logDialogVisible" title="操作日志" width="500px">
      <el-timeline>
        <el-timeline-item v-for="(log, idx) in logs" :key="idx">{{ log }}</el-timeline-item>
      </el-timeline>
      <template #footer>
        <el-button @click="logDialogVisible=false">关闭</el-button>
      </template>
    </el-dialog>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { User } from '@element-plus/icons-vue';
import request from '@/utils/request';
const members = ref([]);
const editDialogVisible = ref(false);
const rechargeDialogVisible = ref(false);
const logDialogVisible = ref(false);
const editForm = ref({});
const rechargeAmount = ref(0);
const logs = ref([]);
const currentMember = ref(null);
const userMap = ref({});

const loadUsers = async () => {
  // 获取所有用户（假设不超过1000条）
  const res = await request.get('/api/users/search', { params: { pageNum: 1, pageSize: 1000 } });
  if (res.data && res.data.data && res.data.data.records) {
    userMap.value = {};
    res.data.data.records.forEach(u => {
      userMap.value[u.id] = u.userName;
    });
  }
};

const loadMembers = async () => {
  try {
    const res = await request.get('/api/users/members');
  members.value = res.data.data;
  } catch (e) {
    members.value = [];
  }
};
const editMember = (row) => {
  editForm.value = { ...row };
  editDialogVisible.value = true;
};
const submitEdit = async () => {
  await request.put(`/api/users/members/${editForm.value.memberId}`, editForm.value);
  ElMessage.success('修改成功');
  editDialogVisible.value = false;
  loadMembers();
};
const recharge = (row) => {
  currentMember.value = row;
  rechargeAmount.value = 0;
  rechargeDialogVisible.value = true;
};
const submitRecharge = async () => {
  await request.post(`/api/users/members/${currentMember.value.memberId}/recharge`, null, { params: { amount: rechargeAmount.value } });
  ElMessage.success('充值成功');
  rechargeDialogVisible.value = false;
  loadMembers();
};
const freeze = async (row) => {
  await ElMessageBox.confirm('确定要冻结该会员吗？', '提示', { type: 'warning' });
  await request.post(`/api/users/members/${row.memberId}/freeze`);
  ElMessage.success('冻结成功');
  loadMembers();
};
const viewLogs = async (row) => {
  const res = await request.get(`/api/users/members/${row.memberId}/logs`);
  logs.value = res.data.data;
  logDialogVisible.value = true;
};
onMounted(() => {
  loadUsers().then(loadMembers);
});
</script>
<style scoped>
.member-manage-bg {
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc 0%, #e0e7ef 100%);
  padding: 32px 0;
}
.member-manage-container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 24px 24px 40px 24px;
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.10);
}
.page-header {
  margin-bottom: 18px;
  background: transparent;
  box-shadow: none;
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
.table-wrapper {
  background: #f9f9f9;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.06);
  padding: 18px 12px 12px 12px;
  margin-bottom: 24px;
  max-width: 100%;
  overflow-x: auto;
}
.el-table {
  background: transparent;
  border-radius: 10px;
  min-width: 900px;
}
@media (max-width: 900px) {
  .member-manage-container {
    padding: 8px;
    max-width: 100vw;
  }
  .table-wrapper {
    padding: 4px;
  }
}
:deep(.el-dialog) {
  border-radius: 12px;
}
:deep(.el-card) {
  background: transparent;
}
</style> 
