<template>
  <div class="admin-book-container orange-theme">
    <!-- 主界面 -->
    <div>
      <!-- 标题和操作栏 -->
      <div class="admin-header">
        <h2>智慧图书交互终端 - 管理员界面</h2>

      </div>

      <!-- 搜索框和添加按钮 -->
      <div class="search-container-wrapper">
        <div class="search-container">
          <input
              v-model="searchKeyword"
              placeholder="输入书名关键词搜索"
              @keyup.enter="searchBooks"
          />
          <button @click="searchBooks">
            <span class="icon">🔍</span> 搜索
          </button>
          <button class="add-btn" @click="openAddDialog">
            <span class="icon">+</span> 添加
          </button>
        </div>
      </div>

      <!-- 图书列表 -->
      <div class="book-list-scroll-container">
        <div class="book-list">
          <transition-group name="book-list" tag="div" class="book-grid">
            <div
                v-for="book in books"
                :key="book.bookId"
                class="book-item"
            >
              <h3 class="title">{{ book.title }}</h3>
              <div class="details">
                <p>作者: {{ book.author }}</p>
                <p>分类: {{ book.category }}</p>
                <p>出版社: {{ book.publisher || '暂无信息' }}</p>
                <p>出版年份: {{ book.publishYear }}</p>
                <p>ISBN: {{ book.isbn }}</p>
              </div>
              <div class="status">
                <span :class="book.status.toLowerCase()">{{ book.status }}</span>
                <p>(可借: {{ book.availableCopies }} / 总数: {{ book.totalCopies }})</p>
              </div>
              <div class="actions">
                <button class="edit-btn" @click="openEditDialog(book)">修改</button>
                <button class="remove-btn" @click="removeBook(book.bookId)">移除</button>
              </div>
            </div>
          </transition-group>
        </div>
      </div>

      <!-- 无结果提示 -->
      <div v-if="books.length === 0" class="no-results">
        <p><span class="icon">⚠️</span> 未找到相关书籍</p>
      </div>
    </div>

    <!-- 弹窗组件 -->
    <EditBookDialog
        v-if="showEditDialog"
        :book="currentBook"
        @close="showEditDialog = false"
        @submit="handleEditSubmit"
    />
    <AddBookDialog
        v-if="showAddDialog"
        @close="showAddDialog = false"
        @submit="handleAddSubmit"
    />
  </div>
</template>

<script>
import EditBookDialog from '../components/EditBookDialog.vue';
import AddBookDialog from '../components/AddBookDialog.vue';
import request from '@/utils/request.js';

export default {
  name: 'AdminBookView',
  components: {
    EditBookDialog,
    AddBookDialog
  },
  data() {
    return {
      searchKeyword: '',
      books: [],
      showEditDialog: false,
      showAddDialog: false,
      currentBook: null
    };
  },
  mounted() {
    this.fetchAllBooks();
  },
  methods: {
    async fetchAllBooks() {
      try {
        const response = await request.get('/api/books/all');
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
        const response = await request.get('/api/books/search', { params: { keyword: this.searchKeyword } });
        this.books = response.data;
      } catch (error) {
        alert('搜索失败: ' + error.message);
      }
    },

    openEditDialog(book) {
      if (!book.bookId || isNaN(Number(book.bookId))) {
        alert('bookId 丢失，无法编辑！');
        return;
      }
      this.currentBook = { ...book, bookId: Number(book.bookId) };
      this.showEditDialog = true;
    },

    openAddDialog() {
      this.showAddDialog = true;
    },

    async removeBook(bookId) {
      if (!confirm('确定要删除这本图书吗？此操作不可撤销！')) return;

      try {
        const res = await request.delete(`/api/books/${bookId}`);
        if (res.data && res.data.code === 200) {
        this.fetchAllBooks();
        alert('删除成功');
        } else {
          alert(res.data.message || '删除失败');
        }
      } catch (error) {
        alert('删除失败: ' + (error.response?.data?.message || error.message || '服务器错误'));
      }
    },

    async handleEditSubmit(updatedBook) {
      if (!updatedBook.bookId || isNaN(Number(updatedBook.bookId))) {
        return;
      }
      try {
        const res = await request.put(
          `/api/books/${updatedBook.bookId}`,
            updatedBook,
            {
              headers: {
                'Content-Type': 'application/json',
                'X-Admin-Password': '2'
              }
            }
        );
        if (res.data && res.data.code === 200) {
        this.showEditDialog = false;
        this.fetchAllBooks();
          alert(res.data.message || '更新成功');
        } else {
          alert(res.data.message || '更新失败');
        }
      } catch (error) {
        alert('更新失败: ' + (error.response?.data?.message || error.message || '服务器错误'));
      }
    },

    async handleAddSubmit(newBook) {
      try {
        const res = await request.post(
            '/api/books/add',
            newBook,
            {
              headers: {
                'Content-Type': 'application/json',
                'X-Admin-Password': '2'
              }
            }
        );
        if (res.data && res.data.code === 200) {
          this.showAddDialog = false;
          this.fetchAllBooks();
          alert(res.data.message || '添加成功');
        } else {
          // 静默处理，不弹窗
        }
      } catch (error) {
        // 静默处理，不弹窗
      }
    },

    logout() {
      this.$emit('logout');
    }
  }
};
</script>

<style scoped>
.admin-book-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 2.5rem 1.5rem;
  background: linear-gradient(135deg, #f8fafc 0%, #e0e7ef 100%);
  border-radius: 18px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.10);
}

.admin-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 18px;
  border-bottom: 2px solid #f39c12;
}

.admin-header h2 {
  color: #d35400;
  margin: 0;
  font-size: 2rem;
  font-weight: 700;
}

.logout-btn {
  padding: 10px 22px;
  background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 600;
  transition: background 0.3s;
}
.logout-btn:hover {
  background: linear-gradient(135deg, #c0392b 0%, #e74c3c 100%);
}

.search-container-wrapper {
  width: 100%;
  display: flex;
  justify-content: flex-start;
  margin-bottom: 2.5rem;
}

.search-container {
  display: flex;
  width: 100%;
  max-width: 800px;
  padding: 18px;
  background: #fff;
  border-radius: 15px;
  box-shadow: 0 2px 10px rgba(243, 156, 18, 0.10);
}

.search-container input {
  flex: 1;
  padding: 1rem;
  border: 2px solid #f8c471;
  border-radius: 10px;
  font-size: 1.1rem;
  transition: all 0.3s;
  max-width: 600px;
  background: #f9f9f9;
}
.search-container input:focus {
  border-color: #e67e22;
  box-shadow: 0 0 0 3px rgba(230, 126, 34, 0.2);
  outline: none;
  background: #fff;
}
.search-container button {
  background: linear-gradient(135deg, #e67e22 0%, #d35400 100%);
  color: white;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem 2rem;
  margin-left: 1rem;
  border-radius: 10px;
  border: none;
  font-weight: 600;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.3s;
}
.add-btn {
  background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%);
  color: white;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem 2rem;
  margin-left: 1rem;
  border-radius: 10px;
  border: none;
  font-weight: 600;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.3s;
}
.search-container button:hover,
.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(230, 126, 34, 0.18);
}

.book-list-scroll-container {
  width: 100%;
  height: 70vh;
  overflow-y: auto;
  padding: 1rem 0;
  border-radius: 8px;
  background: rgba(255, 249, 231, 0.97);
}

.book-list {
  width: 100%;
  padding: 0 1rem;
}

.book-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 2rem;
}

.book-item {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 24px rgba(243, 156, 18, 0.10);
  padding: 2rem 1.5rem 1.5rem 1.5rem;
  display: flex;
  flex-direction: column;
  transition: box-shadow 0.3s, transform 0.3s;
  border: 1px solid #f8c471;
}
.book-item:hover {
  box-shadow: 0 8px 32px rgba(243, 156, 18, 0.18);
  transform: translateY(-4px) scale(1.02);
}
.title {
  color: #d35400;
  margin-bottom: 1.2rem;
  font-size: 1.3rem;
  font-weight: 700;
  border-bottom: 2px solid #f39c12;
  padding-bottom: 0.5rem;
  text-align: center;
}
.details {
  text-align: left;
  padding: 0 0.5rem;
  flex-grow: 1;
  color: #555;
  font-size: 1rem;
}
.details p {
  margin: 0.5rem 0;
}
.status {
  margin: 1.2rem 0 1rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  justify-content: center;
}
.status span {
  padding: 0.3rem 1.2rem;
  border-radius: 16px;
  font-size: 1rem;
  font-weight: 600;
  display: inline-block;
  margin-bottom: 0;
  letter-spacing: 1px;
}
.status .在馆 {
  background: rgba(39, 174, 96, 0.12);
  color: #27ae60;
}
.status .已借出 {
  background: rgba(231, 76, 60, 0.12);
  color: #e74c3c;
}
.actions {
  display: flex;
  gap: 1.2rem;
  margin-top: auto;
  justify-content: center;
}
.edit-btn, .remove-btn {
  flex: 1;
  padding: 0.8rem;
  border-radius: 8px;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
  font-size: 1rem;
}
.edit-btn {
  background: linear-gradient(135deg, #f39c12 0%, #d35400 100%);
}
.remove-btn {
  background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
}
.edit-btn:hover, .remove-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(243, 156, 18, 0.18);
}
.no-results {
  text-align: center;
  padding: 3rem;
  color: #7f8c8d;
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
@media (max-width: 900px) {
  .search-container {
    flex-direction: column;
    align-items: center;
  }
  .search-container input,
  .search-container button,
  .add-btn {
    width: 100%;
    max-width: 500px;
    margin-left: 0;
    margin-top: 10px;
  }
  .book-grid {
    grid-template-columns: 1fr;
  }
  .book-item {
    padding: 1.2rem;
  }
}
@media (max-width: 500px) {
  .actions {
    flex-direction: column;
  }
  .book-item {
    padding: 1rem;
  }
  .admin-header {
    flex-direction: column;
    gap: 15px;
    text-align: center;
  }
}
</style>
