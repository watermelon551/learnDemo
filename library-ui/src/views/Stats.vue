<template>
  <div class="stats-overview">
    <OverviewCards />
    <el-row :gutter="24" class="stats-row">
      <el-col :span="24">
        <BorrowTrend />
      </el-col>
    </el-row>
    <el-row :gutter="24" class="stats-row">
      <el-col :span="12">
        <UserTrend />
      </el-col>
      <el-col :span="12">
        <FinanceTrend />
      </el-col>
    </el-row>
    <!-- 榜单部分下移 -->
    <el-row :gutter="24" class="stats-row">
      <el-col :span="8">
        <BookStats />
      </el-col>
      <el-col :span="8">
        <AuthorStats />
      </el-col>
      <el-col :span="8">
        <GenreStats />
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import OverviewCards from '@/components/OverviewCards.vue';
import BorrowTrend from '@/components/BorrowTrend.vue';
import UserTrend from '@/components/UserTrend.vue';
import FinanceTrend from '@/components/FinanceTrend.vue';
import BookStats from '@/components/BookStats.vue';
import AuthorStats from '@/components/AuthorStats.vue';
import GenreStats from '@/components/GenreStats.vue';
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
