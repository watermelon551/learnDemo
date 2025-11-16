<template>
  <div class="hot-overview">
    <div class="hot-header">
      <h2><el-icon><Star /></el-icon> 时下热门排行榜</h2>
      <div class="hot-desc">本榜单展示当前借阅最火的图书、作者和分类，帮助你发现图书馆的热门资源。</div>
    </div>
    <el-row :gutter="24" class="hot-row">
      <el-col :span="24">
        <BookStats />
      </el-col>
    </el-row>
    <el-row :gutter="24" class="hot-row">
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
import { Star } from '@element-plus/icons-vue';
</script>
<style scoped>
.hot-overview {
  padding: 32px 24px 24px 24px;
  background: #f5f7fa;
  min-height: 100vh;
}
.hot-header {
  margin-bottom: 28px;
  text-align: center;
}
.hot-header h2 {
  font-size: 2rem;
  font-weight: 700;
  color: #409eff;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 10px;
}
.hot-desc {
  font-size: 1.1rem;
  color: #666;
  margin-bottom: 0;
}
.hot-row {
  margin-bottom: 24px;
}
</style> 