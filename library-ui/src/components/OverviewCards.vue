<template>
  <el-row :gutter="24" class="overview-row">
    <el-col :span="6" v-for="item in cards" :key="item.label">
      <el-card class="overview-card" shadow="hover">
        <div class="overview-label">{{ item.label }}</div>
        <div class="overview-value">{{ item.value }}</div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import request from '@/utils/request';
const cards = ref([
  { label: '总用户数', value: 0 },
  { label: '总图书数', value: 0 },
  { label: '总借阅数', value: 0 },
  { label: '总收入(元)', value: 0 }
]);
const loadOverview = async () => {
  const res = await request.get('/api/stats/overview');
  if (res.data) {
    cards.value[0].value = res.data.totalUsers;
    cards.value[1].value = res.data.totalBooks;
    cards.value[2].value = res.data.totalBorrows;
    cards.value[3].value = res.data.totalIncome;
  }
};
onMounted(loadOverview);
</script>

<style scoped>
.overview-row {
  margin-bottom: 24px;
}
.overview-card {
  border-radius: 14px;
  text-align: center;
  min-height: 90px;
  background: linear-gradient(135deg, #e0e7ef 0%, #f8fafc 100%);
}
.overview-label {
  font-size: 1.1rem;
  color: #888;
  margin-bottom: 6px;
}
.overview-value {
  font-size: 2.1rem;
  font-weight: bold;
  color: #409eff;
}
</style> 