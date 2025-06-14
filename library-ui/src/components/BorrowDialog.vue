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
        <div class="form-group">
          <label>用户名</label>
          <input v-model="form.username" required>
        </div>

        <div class="form-group">
          <label>密码</label>
          <input v-model="form.password" type="password" required>
        </div>

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

        <div class="form-group">
          <label>联系方式</label>
          <input v-model="form.contact" required>
        </div>

        <button type="submit" class="submit-btn">
          {{ activeTab === 'borrow' ? '确认借书' : '确认预约' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    bookId: Number,
    title: String
  },
  data() {
    return {
      activeTab: 'borrow',
      form: {
        username: '',
        password: '',
        days: '30',
        reserveDate: '',
        contact: ''
      }
    };
  },
  methods: {
    closeDialog() {
      this.$emit('close');
    },
    submitForm() {
      const request = {
        username: this.form.username,
        password: this.form.password,
        bookId: this.bookId,
        type: this.activeTab === 'borrow' ? '借书' : '预约'
      };

      fetch('http://localhost:8080/api/books/borrow', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(request)
      })
          .then(res => {
            // 克隆响应以便多次使用
            const responseClone = res.clone();

            // 状态码检查（如果状态不是成功）
            if (!res.ok) {
              return res.text().then(text => {
                throw new Error(text);
              });
            }

            // 尝试解析为 JSON
            return res.json()
                .then(data => {
                  // 成功JSON解析的处理逻辑
                  if (this.activeTab === 'borrow' && data.record) {
                    const record = data.record;
                    const receipt = `
            ===== 借书凭证 =====
            凭证ID: ${record.id}
            用户ID: ${record.userId}
            图书ID: ${record.bookId}
            借书时间: ${record.borrowDate}
            最晚归还时间: ${record.dueDate}
            凭证生成时间: ${record.createdAt}
            ===================
          `;
                    alert(receipt);
                  } else {
                    // 预约操作只显示简单消息
                    alert(data.message);
                  }

                  this.$emit('borrow-success');
                  this.closeDialog();
                })
                .catch(jsonError => {
                  // 如果JSON解析失败，使用克隆的响应读取文本
                  return responseClone.text().then(text => {
                    // 显示原始响应文本
                    alert(`操作成功：${text}`);
                    this.$emit('borrow-success');
                    this.closeDialog();
                  });
                });
          })
          .catch(err => {
            // 处理错误
            let errorMessage = err.message;

            // 修正错误消息以符合要求
            if (errorMessage.includes("用户认证失败")) {
              errorMessage = "当前用户名与数据库不匹配，请求失败！";
            }

            alert(`操作失败：${errorMessage}`);
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
