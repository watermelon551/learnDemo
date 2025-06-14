<template>
  <div class="financial-management">
    <el-card class="page-header" :body-style="{ display: 'none' }">
      <template #header>
        <div class="card-header">
          <h2>财务管理</h2>
        </div>
      </template>
    </el-card>

    <el-row :gutter="20" class="operation-sections">
      <!-- 押金管理 -->
      <el-col :span="8">
        <el-card class="operation-card">
          <template #header>
            <div class="card-header">
              <h3><el-icon><Money /></el-icon> 押金管理</h3>
            </div>
          </template>
          <el-form :model="deposit" label-position="top">
            <el-form-item label="用户名">
              <el-input v-model="deposit.username" placeholder="请输入用户名">
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="操作类型">
              <el-select v-model="deposit.operation" class="w-100">
                <el-option label="收取押金" value="charge" />
                <el-option label="退还押金" value="refund" />
              </el-select>
            </el-form-item>
            <el-form-item label="金额">
              <el-input-number
                v-model="deposit.amount"
                :min="0.01"
                :step="0.01"
                :precision="2"
                class="w-100"
              />
            </el-form-item>
            <el-form-item label="备注">
              <el-input
                v-model="deposit.description"
                type="textarea"
                :rows="2"
                placeholder="请输入备注信息"
              />
            </el-form-item>
            <el-button
              type="primary"
              @click="handleDeposit"
              :loading="depositLoading"
              class="w-100"
            >
              {{ depositLoading ? '处理中...' : '提交' }}
            </el-button>
          </el-form>
        </el-card>
      </el-col>

      <!-- 预存款管理 -->
      <el-col :span="8">
        <el-card class="operation-card">
          <template #header>
            <div class="card-header">
              <h3><el-icon><Wallet /></el-icon> 预存款管理</h3>
            </div>
          </template>
          <el-form :model="prepayment" label-position="top">
            <el-form-item label="用户名">
              <el-input v-model="prepayment.username" placeholder="请输入用户名">
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="操作类型">
              <el-select v-model="prepayment.operation" class="w-100">
                <el-option label="充值" value="charge" />
                <el-option label="退款" value="refund" />
              </el-select>
            </el-form-item>
            <el-form-item label="金额">
              <el-input-number
                v-model="prepayment.amount"
                :min="0.01"
                :step="0.01"
                :precision="2"
                class="w-100"
              />
            </el-form-item>
            <el-form-item label="备注">
              <el-input
                v-model="prepayment.description"
                type="textarea"
                :rows="2"
                placeholder="请输入备注信息"
              />
            </el-form-item>
            <el-button
              type="primary"
              @click="handlePrepayment"
              :loading="prepaymentLoading"
              class="w-100"
            >
              {{ prepaymentLoading ? '处理中...' : '提交' }}
            </el-button>
          </el-form>
        </el-card>
      </el-col>

      <!-- 罚款管理 -->
      <el-col :span="8">
        <el-card class="operation-card">
          <template #header>
            <div class="card-header">
              <h3><el-icon><Warning /></el-icon> 罚款管理</h3>
            </div>
          </template>
          <el-form :model="fine" label-position="top">
            <el-form-item label="用户名">
              <el-input v-model="fine.username" placeholder="请输入用户名">
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item label="罚款原因">
              <el-select v-model="fine.reason" class="w-100">
                <el-option label="超期罚款" value="overdue" />
                <el-option label="丢失罚款" value="lost" />
                <el-option label="损毁罚款" value="damaged" />
              </el-select>
            </el-form-item>
            <el-form-item label="金额">
              <el-input-number
                v-model="fine.amount"
                :min="0.01"
                :step="0.01"
                :precision="2"
                class="w-100"
              />
            </el-form-item>
            <el-form-item label="备注">
              <el-input
                v-model="fine.description"
                type="textarea"
                :rows="2"
                placeholder="请输入备注信息"
              />
            </el-form-item>
            <el-button
              type="primary"
              @click="handleFine"
              :loading="fineLoading"
              class="w-100"
            >
              {{ fineLoading ? '处理中...' : '提交' }}
            </el-button>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <!-- 财务记录查询 -->
    <el-card class="records-section">
      <template #header>
        <div class="card-header">
          <h3><el-icon><Document /></el-icon> 财务记录查询</h3>
        </div>
      </template>
      <el-form :inline="true" class="query-form">
        <el-form-item label="查询方式">
          <el-select v-model="queryType" :placeholder="queryTypeLabel" @change="handleQueryTypeChange">
            <el-option label="按用户名" value="user" />
            <el-option label="按记录类型" value="type" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="queryType === 'user'" label="用户名">
          <el-input v-model="queryUsername" placeholder="请输入用户名">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item v-else label="记录类型">
          <el-select v-model="queryRecordType">
            <el-option label="押金" value="deposit" />
            <el-option label="预存款" value="prepayment" />
            <el-option label="罚款" value="fine" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchRecords" :loading="tableLoading">
            <el-icon><Search /></el-icon>
            {{ tableLoading ? '查询中...' : '查询' }}
          </el-button>
        </el-form-item>
      </el-form>

      <el-table
        v-loading="tableLoading"
        :data="records"
        style="width: 100%"
        border
        stripe
        highlight-current-row
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.type)" disable-transitions>
              {{ formatType(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-tag :type="getOperationTagType(row.operation)" disable-transitions>
              {{ formatOperation(row.operation) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            <span class="amount">¥{{ row.amount.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createTime" label="时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
      </el-table>

      <div v-if="records.length === 0 && !tableLoading" class="empty-data">
        <el-empty description="暂无记录" />
      </div>
    </el-card>
  </div>
</template>

<script>
import { handleDeposit, handlePrepayment, handleFine, getRecordsByUser, getRecordsByType } from '@/api/financial.js';
import { ElMessage } from 'element-plus';
import { Money, Wallet, Warning, Document, User, Search } from '@element-plus/icons-vue';

export default {
  components: {
    Money,
    Wallet,
    Warning,
    Document,
    User,
    Search
  },
  data() {
    return {
      deposit: {
        username: '',
        operation: 'charge',
        amount: null,
        description: ''
      },
      prepayment: {
        username: '',
        operation: 'charge',
        amount: null,
        description: ''
      },
      fine: {
        username: '',
        reason: 'overdue',
        amount: null,
        description: ''
      },
      queryType: 'user',
      queryUsername: '',
      queryRecordType: 'deposit',
      records: [],
      depositLoading: false,
      prepaymentLoading: false,
      fineLoading: false,
      tableLoading: false
    };
  },
  computed: {
    queryTypeLabel() {
      return this.queryType === 'user' ? '按用户名' : this.queryType === 'type' ? '按记录类型' : '请选择';
    }
  },
  methods: {
    // 押金操作
    async handleDeposit() {
      try {
        this.depositLoading = true;
        this.validateInput(this.deposit);

        await handleDeposit({
          username: this.deposit.username,
          operation: this.deposit.operation,
          amount: this.deposit.amount,
          description: this.deposit.description || undefined
        });

        ElMessage.success('押金操作成功');
        this.resetForm('deposit');
        this.fetchRecords();
      } catch (error) {
        this.handleError(error, '押金操作失败');
      } finally {
        this.depositLoading = false;
      }
    },

    // 预存款操作
    async handlePrepayment() {
      try {
        this.prepaymentLoading = true;
        this.validateInput(this.prepayment);

        await handlePrepayment({
          username: this.prepayment.username,
          operation: this.prepayment.operation,
          amount: this.prepayment.amount,
          description: this.prepayment.description || undefined
        });

        ElMessage.success('预存款操作成功');
        this.resetForm('prepayment');
        this.fetchRecords();
      } catch (error) {
        this.handleError(error, '预存款操作失败');
      } finally {
        this.prepaymentLoading = false;
      }
    },

    // 罚款操作
    async handleFine() {
      try {
        this.fineLoading = true;
        this.validateInput(this.fine);

        await handleFine({
          username: this.fine.username,
          reason: this.fine.reason,
          amount: this.fine.amount,
          description: this.fine.description || undefined
        });

        ElMessage.success('罚款操作成功');
        this.resetForm('fine');
        this.fetchRecords();
      } catch (error) {
        this.handleError(error, '罚款操作失败');
      } finally {
        this.fineLoading = false;
      }
    },

    // 查询记录
    async fetchRecords() {
      try {
        this.tableLoading = true;
        this.records = [];

        if (this.queryType === 'user' && !this.queryUsername) {
          // 查询用户名为空时不抛异常，直接返回，避免弹窗
          return;
        }

        const response = this.queryType === 'user'
          ? await getRecordsByUser(this.queryUsername)
          : await getRecordsByType(this.queryRecordType);

        this.records = response.data;
      } catch (error) {
        this.handleError(error, '查询记录失败');
      } finally {
        this.tableLoading = false;
      }
    },

    // 输入验证
    validateInput(data) {
      if (!data.username) {
        throw new Error('请输入用户名');
      }
      if (!data.amount || isNaN(data.amount) || data.amount <= 0) {
        throw new Error('请输入有效的金额（必须大于0）');
      }
    },

    // 重置表单
    resetForm(formType) {
      if (formType === 'deposit') {
        this.deposit = {
          ...this.deposit,
          amount: null,
          description: ''
        };
      } else if (formType === 'prepayment') {
        this.prepayment = {
          ...this.prepayment,
          amount: null,
          description: ''
        };
      } else if (formType === 'fine') {
        this.fine = {
          ...this.fine,
          amount: null,
          description: ''
        };
      }
    },

    // 错误处理
    handleError(error, defaultMsg = '操作失败') {
      let errorMsg = defaultMsg;
      if (error.response) {
        errorMsg += `: ${error.response.data?.message || error.response.statusText}`;
      } else if (error.request) {
        errorMsg += ': 服务器无响应';
      } else {
        errorMsg += `: ${error.message}`;
      }
      ElMessage.error(errorMsg);
      console.error('Error details:', error);
    },

    // 格式化类型显示
    formatType(type) {
      const types = {
        deposit: '押金',
        prepayment: '预存款',
        fine: '罚款'
      };
      return types[type] || type;
    },

    // 格式化操作显示
    formatOperation(operation) {
      const operations = {
        charge: '收取',
        refund: '退还'
      };
      return operations[operation] || operation;
    },

    // 格式化日期
    formatDate(timestamp) {
      if (!timestamp) return '无日期';
      return new Date(timestamp).toLocaleString();
    },

    // 获取类型标签样式
    getTypeTagType(type) {
      const types = {
        deposit: 'success',
        prepayment: 'warning',
        fine: 'danger'
      };
      return types[type] || 'info';
    },

    // 获取操作标签样式
    getOperationTagType(operation) {
      const types = {
        charge: 'success',
        refund: 'info'
      };
      return types[operation] || 'info';
    },

    // 处理查询类型变化
    handleQueryTypeChange() {
      this.queryUsername = '';
      this.queryRecordType = 'deposit';
      this.records = [];
    }
  }
};
</script>

<style scoped>
.financial-management {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-header h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #303133;
}

.card-header h3 {
  margin: 0;
  font-size: 1.2rem;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 8px;
}

.operation-sections {
  margin-bottom: 20px;
}

.operation-card {
  height: 100%;
}

.w-100 {
  width: 100%;
}

.records-section {
  margin-top: 20px;
}

.query-form {
  margin-bottom: 20px;
}

.amount {
  font-weight: bold;
  color: #409EFF;
}

.empty-data {
  padding: 40px 0;
}

:deep(.el-card__header) {
  padding: 15px 20px;
  border-bottom: 1px solid #EBEEF5;
  background-color: #F5F7FA;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-input-number) {
  width: 100%;
}

:deep(.el-table) {
  margin-top: 20px;
}

:deep(.el-tag) {
  text-align: center;
  min-width: 60px;
}
</style>
