<template>
  <el-card class="stats-card" shadow="hover">
    <template #header>
      <div class="stats-title">
        <el-icon><Reading /></el-icon> Top 10 图书借阅榜
      </div>
    </template>
    <div class="filter-controls">
      <el-button @click="loadAllBooks" type="primary" size="small" :plain="!selectedGenre && !selectedAuthor">全部</el-button>
      <el-select v-model="selectedGenre" @change="loadBooksByGenre" placeholder="按分类筛选" clearable size="small" class="filter-select">
        <el-option v-for="genre in genres" :key="genre" :label="genre" :value="genre" />
      </el-select>
      <el-select v-model="selectedAuthor" @change="loadBooksByAuthor" placeholder="按作者筛选" clearable size="small" class="filter-select">
        <el-option v-for="author in authors" :key="author" :label="author" :value="author" />
      </el-select>
    </div>
    <el-table :data="books" stripe border class="stats-table" style="margin-top: 10px;">
      <el-table-column label="排名" type="index" width="70" align="center" />
      <el-table-column prop="bookTitle" label="书名" min-width="120" />
      <el-table-column prop="author" label="作者" min-width="100" />
      <el-table-column prop="genre" label="分类" min-width="100" />
      <el-table-column prop="borrowCount" label="借阅次数" min-width="100" align="center">
        <template #default="{ row }">
          <el-tag type="success" disable-transitions>{{ row.borrowCount }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="books.length === 0" description="暂无数据" />
  </el-card>
</template>

<script setup>
import statsApi from '../api/stats.js';
import { ref, onMounted } from 'vue';
import { Reading } from '@element-plus/icons-vue';

const books = ref([]);
const genres = ref([]);
const authors = ref([]);
const selectedGenre = ref('');
const selectedAuthor = ref('');

const loadAllBooks = async () => {
  const response = await statsApi.getTop10Books();
  books.value = response.data;
  selectedGenre.value = '';
  selectedAuthor.value = '';
};
const loadBooksByGenre = async () => {
  if (selectedGenre.value) {
    const response = await statsApi.getTop10BooksByGenre(selectedGenre.value);
    books.value = response.data;
    selectedAuthor.value = '';
  }
};
const loadBooksByAuthor = async () => {
  if (selectedAuthor.value) {
    const response = await statsApi.getTop10BooksByAuthor(selectedAuthor.value);
    books.value = response.data;
    selectedGenre.value = '';
  }
};
const loadGenresAndAuthors = async () => {
  const [genresRes, authorsRes] = await Promise.all([
    statsApi.getTop10Genres(),
    statsApi.getTop10Authors()
  ]);
  genres.value = genresRes.data.map(g => g.genre);
  authors.value = authorsRes.data.map(a => a.author);
};
onMounted(async () => {
  await loadAllBooks();
  await loadGenresAndAuthors();
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
.filter-controls {
  margin-bottom: 10px;
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}
.filter-select {
  min-width: 140px;
}
.stats-table {
  border-radius: 8px;
  overflow: hidden;
}
</style>
