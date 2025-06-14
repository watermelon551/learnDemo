<template>
  <div class="stats-overview">
    <el-card class="page-header":body-style="{ display: 'none' }">
      <template #header>
        <div class="card-header">
          <h2><el-icon><DataLine /></el-icon> 统计分析</h2>
        </div>
      </template>
    </el-card>
    <el-row :gutter="24" class="stats-row">
      <el-col :span="24">
        <BookStats />
      </el-col>
    </el-row>
    <el-row :gutter="24" class="stats-row">
      <el-col :span="12">
        <AuthorStats />
      </el-col>
      <el-col :span="12">
        <GenreStats />
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import BookStats from '@/components/BookStats.vue';
import AuthorStats from '@/components/AuthorStats.vue';
import GenreStats from '@/components/GenreStats.vue';
import { DataLine } from '@element-plus/icons-vue';
</script>

<style scoped>
.stats-overview {
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
.stats-row {
  margin-bottom: 24px;
}
</style>
