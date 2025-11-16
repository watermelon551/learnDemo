<template>
  <div class="dialog-overlay" @click.self="closeDialog">
    <div class="dialog">
      <button class="close-btn" @click="closeDialog">×</button>

      <h2>{{ title }}</h2>

      <div class="tabs">
        <button
            :class="{ active: activeTab === 'borrow' }"
            @click="activeTab = 'borrow'"
        >
          借书
        </button>
        <button
            :class="{ active: activeTab === 'reserve' }"
            @click="activeTab = 'reserve'"
        >
          预约
        </button>
      </div>

      <form @submit.prevent="submitForm">
        <div v-if="activeTab === 'borrow'" class="form-group">
          <label>借阅天数</label>
          <select v-model="form.days">
            <option value="15">15天</option>
            <option value="30" selected>30天</option>
            <option value="60">60天</option>
          </select>
        </div>

        <div v-if="activeTab === 'reserve'" class="form-group">
          <label>预约截止日期</label>
          <input type="date" v-model="form.reserveDate" required>
        </div>

        <button type="submit" class="submit-btn">
          {{ activeTab === 'borrow' ? '确认借书' : '确认预约' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script>
import { useUserStore } from '@/stores/userStore';

export default {
  props: {
    bookId: Number,
    title: String
  },
  data() {
    return {
      activeTab: 'borrow',
      form: {
        days: '30',
        reserveDate: '',
      }
    };
  },
  methods: {
    closeDialog() {
      this.$emit('close');
    },
    submitForm() {
      const token = localStorage.getItem('token');
      fetch(`http://localhost:8080/api/borrow/${this.bookId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          ...(token ? { 'Authorization': `Bearer ${token}` } : {})
        },
      })
        .then(res => {
          if (!res.ok) {
            return res.text().then(text => { throw new Error(text); });
          }
          return res.text();
        })
        .then(msg => {
          alert(msg);
          this.closeDialog();
          this.$emit('borrow-success');
        })
        .catch(err => {
          alert('借书失败: ' + err.message);
        });
    }
  }
};
</script>

<style scoped>
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000; /* 提高z-index确保在最上层 */
  overflow-y: auto;
  backdrop-filter: blur(3px);
}

.dialog {
  position: fixed; /* 改为固定定位 */
  top: 50%;      /* 在视口垂直居中 */
  left: 50%;     /* 在视口水平居中 */
  transform: translate(-50%, -50%); /* 微调位置 */
  background: white;
  border-radius: 12px;
  padding: 30px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh; /* 最大高度不超过视口高度 */
  overflow-y: auto; /* 弹窗内部可滚动 */
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.3);
  z-index: 2001;
  animation: dialog-appear 0.3s ease-out;
}

@keyframes dialog-appear {
  0% {
    opacity: 0;
    transform: translate(-50%, -40%);
  }
  100% {
    opacity: 1;
    transform: translate(-50%, -50%);
  }
}

.close-btn {
  position: absolute;
  top: 15px;
  right: 15px;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #777;
}

.tabs {
  display: flex;
  margin-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.tabs button {
  padding: 10px 20px;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 16px;
  position: relative;
  bottom: -1px;
}

.tabs button.active {
  border-bottom: 3px solid #3498db;
  color: #3498db;
  font-weight: bold;
}

.form-group {
  margin-bottom: 18px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: 500;
  color: #333;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 15px;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(52, 152, 219, 0.4);
}
</style>
