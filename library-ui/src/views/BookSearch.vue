<template>
  <div class="book-search-container">
    <!-- 只保留主界面，无需认证模块 -->
    <div>
      <!-- 搜索框居中容器 -->
      <div class="search-container-wrapper">
        <div class="search-container">
          <input
              v-model="searchKeyword"
              placeholder="输入书名或作者关键词搜索"
              @keyup.enter="searchBooks"
          />
          <button @click="searchBooks" class="search-btn">
            <span class="icon">🔍</span> 搜索
          </button>
          <!-- 新增还书主按钮 -->
          <button @click="openReturnDialog" class="return-main-btn">
            <span class="icon">📚</span> 还书
          </button>
        </div>
      </div>

      <!-- 图书列表滚动容器 -->
      <div class="book-list-scroll-container">
        <div class="book-list">
          <transition-group name="book-list" tag="div" class="book-grid">
            <div
                v-for="book in books"
                :key="book.bookId"
                class="book-item card"
            >
              <!-- 彩色封面 -->
              <div class="book-cover" :style="getCoverStyle(book.category)">
                <span class="cover-title">{{ book.title.slice(0, 5) }}</span>
              </div>
              <h3 class="title">{{ book.title }}</h3>
              <div class="details">
                <p>作者：{{ book.author }}</p>
                <p>分类：{{ book.category }}</p>
                <p>出版社：{{ book.publisher || '暂无信息' }}</p>
                <p>出版年份：{{ book.publishYear }}</p>
              </div>

              <div class="status">
                <span :class="['status-badge', book.status === '在馆' ? 'available' : 'borrowed']">
                  {{ book.status }}
                </span>
                <span class="copies">可借 {{ book.availableCopies }} / 总数 {{ book.totalCopies }}</span>
              </div>
              <div class="actions">
                <button
                    class="borrow-btn"
                    @click="openBorrowDialog(book.bookId, book.title)"
                    :disabled="book.availableCopies === 0"
                >
                  借书
                </button>
              </div>
            </div>
          </transition-group>
        </div>
      </div>
    </div>

    <!-- 弹窗组件 -->
    <BorrowDialog
        v-if="showBorrowDialog"
        :book-id="currentBookId"
        :title="currentBookTitle"
        @close="showBorrowDialog = false"
        @borrow-success="searchBooks"
    />
    <!-- 新还书弹窗 -->
    <ReturnDialog
        v-if="showReturnDialog"
        :borrow-list="myBorrowList"
        @close="showReturnDialog = false"
        @return-success="handleReturnSuccess"
    />
  </div>
</template>

<script>
import BorrowDialog from '../components/BorrowDialog.vue';
import ReturnDialog from '../components/ReturnDialog.vue';
import request from '@/utils/request.js';

// 分类颜色映射
const categoryColors = {
  '文学': '#409EFF',
  '历史': '#67C23A',
  '心理学': '#E6A23C',
  '科幻': '#F56C6C',
  '哲学': '#909399',
  '计算机': '#1abc9c',
  '经济': '#e67e22',
  '艺术': '#8e44ad',
  '教育': '#2d8cf0',
  '其他': '#606266'
};

export default {
  components: {
    BorrowDialog,
    ReturnDialog
  },
  data() {
    return {
      searchKeyword: '',
      books: [],
      showBorrowDialog: false,
      showReturnDialog: false,
      currentBookId: null,
      currentBookTitle: '',
      myBorrowList: [] // 新增：我的借阅列表
    };
  },
  mounted() {
    this.fetchAllBooks();
  },
  methods: {
    handleReturnSuccess() {
      this.searchBooks();
      this.showReturnDialog = false;
    },
    async fetchAllBooks() {
      try {
        // 获取所有书籍（包括可借为0的）
        const response = await request.get('/api/books?showAll=1');
        this.books = response.data;
      } catch (error) {
        alert('获取数据失败: ' + error.message);
      }
    },
    async searchBooks() {
      if (!this.searchKeyword.trim()) {
        this.fetchAllBooks();
        return;
      }
      try {
        // 支持书名和作者搜索
        const response = await request.get('/api/books/search', { params: { keyword: this.searchKeyword } });
        this.books = response.data;
      } catch (error) {
        alert('搜索失败: ' + error.message);
      }
    },
    openBorrowDialog(bookId, bookTitle) {
      this.currentBookId = bookId;
      this.currentBookTitle = bookTitle;
      this.showBorrowDialog = true;
    },
    // 新增：打开还书弹窗并加载我的借阅
    async openReturnDialog() {
      try {
        const res = await request.get('/api/borrow/my');
        this.myBorrowList = res.data;
      this.showReturnDialog = true;
      } catch (e) {
        alert('获取借阅记录失败');
      }
    },
    getCoverStyle(category) {
      const color = categoryColors[category] || categoryColors['其他'];
      return {
        background: color,
        borderRadius: '12px',
        height: '80px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        marginBottom: '1.2rem',
        boxShadow: '0 2px 8px rgba(0,0,0,0.08)'
      };
    }
  }
};
</script>

<style scoped>
/* 整体容器居中（未改动） */
.book-search-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 1rem;
  background: linear-gradient(135deg, #f8fafc 0%, #e0e7ef 100%);
  border-radius: 18px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.10);
}

/* 搜索框居中容器（未改动） */
.search-container-wrapper {
  width: 100%;
  display: flex;
  justify-content: center;
  margin-bottom: 2rem;
}

.search-container {
  display: flex;
  width: 100%;
  max-width: 600px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(52, 152, 219, 0.08);
  padding: 0.5rem 1rem;
  align-items: center;
}

.search-container input {
  flex: 1;
  padding: 0.8rem 1rem;
  border: none;
  border-radius: 8px;
  font-size: 1.1rem;
  background: transparent;
  outline: none;
}

.search-btn {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 0.7rem 1.5rem;
  font-size: 1rem;
  font-weight: 600;
  margin-left: 1rem;
  cursor: pointer;
  transition: background 0.3s, box-shadow 0.3s;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.10);
}

.search-btn:hover {
  background: linear-gradient(135deg, #66b1ff 0%, #409eff 100%);
}

.book-list-scroll-container {
  width: 100%;
  max-width: 1200px;
  height: 70vh;
  overflow-y: auto;
  padding: 1rem 0;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.95);
}

/* 🔧 图书列表调整 */
.book-list {
  width: 100%;
  padding: 0 1rem;
}

/* 🔧 图书网格调整（限制行高） */
.book-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 2rem;
}

/* 图书列表过渡动画（未改动） */
.book-list-enter-active,
.book-list-leave-active {
  transition: all 0.5s ease;
}
.book-list-enter-from,
.book-list-leave-to {
  opacity: 0;
  transform: translateY(30px);
}
.book-list-move {
  transition: transform 0.5s ease;
}

/* 单个图书卡片（未改动） */
.card.book-item {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(52, 152, 219, 0.10);
  padding: 2rem 1.5rem 1.5rem 1.5rem;
  display: flex;
  flex-direction: column;
  transition: box-shadow 0.3s, transform 0.3s;
  border: 1px solid #e0e7ef;
}

.card.book-item:hover {
  box-shadow: 0 8px 32px rgba(52, 152, 219, 0.18);
  transform: translateY(-4px) scale(1.02);
}

.title {
  color: #222;
  margin-bottom: 1.2rem;
  font-size: 1.3rem;
  font-weight: 700;
  border-bottom: 2px solid #409eff;
  padding-bottom: 0.5rem;
  text-align: center;
}

/* 图书详细信息居左对齐（未改动） */
.details {
  text-align: left;
  padding: 0 0.5rem;
  flex-grow: 1;
  color: #555;
  font-size: 1rem;
}

.details p {
  margin: 0.8rem 0;
}

.status {
  margin: 1.2rem 0 1rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  justify-content: center;
}

.status-badge {
  padding: 0.3rem 1.2rem;
  border-radius: 16px;
  font-size: 1rem;
  font-weight: 600;
  display: inline-block;
  margin-bottom: 0;
  letter-spacing: 1px;
}

.status-badge.available {
  background: rgba(39, 174, 96, 0.12);
  color: #27ae60;
}

.status-badge.borrowed {
  background: rgba(231, 76, 60, 0.12);
  color: #e74c3c;
}

.copies {
  color: #888;
  font-size: 0.98rem;
}

.actions {
  display: flex;
  gap: 1.2rem;
  margin-top: auto;
  justify-content: center;
}

.borrow-btn, .return-btn {
  padding: 0.8rem 1.6rem;
  border-radius: 8px;
  font-weight: 600;
  font-size: 1rem;
  border: none;
  cursor: pointer;
  transition: all 0.3s;
}

.borrow-btn {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  color: white;
}

.return-btn {
  background: linear-gradient(135deg, #2ecc71 0%, #27ae60 100%);
  color: white;
}

.borrow-btn:hover, .return-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
}

button:disabled {
  background: #cbd5e0 !important;
  cursor: not-allowed;
  opacity: 0.7;
}

/* 无结果提示居中（未改动） */
.no-results {
  text-align: center;
  padding: 3rem;
  color: #718096;
  font-size: 1.3rem;
  width: 100%;
}

.no-results p {
  display: inline-flex;
  align-items: center;
  gap: 0.8rem;
  padding: 1.2rem 2.5rem;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.08);
}

.icon {
  font-size: 1.2rem;
}

/* 响应式调整（优化小屏幕显示） */
@media (max-width: 900px) {
  .book-grid {
    grid-template-columns: 1fr;
  }
  .search-container {
    flex-direction: column;
    align-items: center;
    padding: 15px;
  }
  .search-container input,
  .search-btn {
    width: 100%;
    max-width: 500px;
    margin-left: 0;
    margin-top: 10px;
  }
  .card.book-item {
    padding: 1.2rem;
  }
}

@media (max-width: 500px) {
  .actions {
    flex-direction: column;
    gap: 0.8rem;
  }
  .card.book-item {
    padding: 1rem;
  }
}

.book-cover {
  width: 100%;
  min-height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1.2rem;
  border-radius: 12px;
  font-size: 2rem;
  font-weight: bold;
  color: #fff;
  letter-spacing: 2px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}
.cover-title {
  display: block;
  width: 100%;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.return-main-btn {
  background: linear-gradient(135deg, #67c23a 0%, #409eff 100%);
  color: #fff;
  border: none;
  border-radius: 8px;
  padding: 0.7rem 1.5rem;
  font-size: 1rem;
  font-weight: 600;
  margin-left: 1rem;
  cursor: pointer;
  transition: background 0.3s, box-shadow 0.3s;
  box-shadow: 0 2px 8px rgba(103, 194, 58, 0.10);
}
.return-main-btn:hover {
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
}
</style>

