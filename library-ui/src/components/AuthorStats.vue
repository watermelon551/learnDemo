<template>
  <el-card class="stats-card" shadow="hover">
    <template #header>
      <div class="stats-title">
        <el-icon><User /></el-icon> Top 10 作者借阅榜
      </div>
    </template>
    <el-table :data="authors" stripe border class="stats-table" style="margin-top: 10px;">
      <el-table-column label="排名" type="index" width="70" align="center" />
      <el-table-column prop="author" label="作者" min-width="120" />
      <el-table-column prop="borrowCount" label="借阅次数" min-width="120" align="center">
        <template #default="{ row }">
          <el-tag type="warning" disable-transitions>{{ row.borrowCount }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="authors.length === 0" description="暂无数据" />
  </el-card>
</template>

<script setup>
import statsApi from '../api/stats.js';
import { ref, onMounted } from 'vue';
import { User } from '@element-plus/icons-vue';

const authors = ref([]);
const loadAuthors = async () => {
  const response = await statsApi.getTop10Authors();
  authors.value = response.data;
};
onMounted(() => {
  loadAuthors();
  setInterval(loadAuthors, 30000);
});
</script>

<style scoped>
.stats-card {
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(64, 158, 255, 0.08);
  margin-bottom: 24px;
}
.stats-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 8px;
}
.stats-table {
  border-radius: 8px;
  overflow: hidden;
}
</style>
