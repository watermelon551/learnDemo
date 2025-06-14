<template>
  <div class="dialog-overlay" @click.self="closeDialog">
    <div class="dialog">
      <button class="close-btn" @click="closeDialog">×</button>
      <h2>编辑图书信息</h2>
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label>书名<span class="required">*</span></label>
          <input v-model="form.title" required>
        </div>
        <div class="form-group">
          <label>作者<span class="required">*</span></label>
          <input v-model="form.author" required>
        </div>
        <div class="form-group">
          <label>分类<span class="required">*</span></label>
          <input v-model="form.category" required>
        </div>
        <div class="form-group">
          <label>出版社</label>
          <input v-model="form.publisher">
        </div>
        <div class="form-group">
          <label>出版年份</label>
          <input v-model="form.publishYear" type="number" min="1900" :max="new Date().getFullYear()">
        </div>
        <div class="form-group">
          <label>ISBN<span class="required">*</span></label>
          <input v-model="form.isbn" required>
        </div>
        <div class="form-group">
          <label>总副本数<span class="required">*</span></label>
          <input
              v-model="form.totalCopies"
              type="number"
              min="1"
              required
              @input="updateAvailableCopies"
          >
        </div>
        <div class="form-group">
          <label>可用副本数<span class="required">*</span></label>
          <input
              v-model="form.availableCopies"
              type="number"
              :min="0"
              :max="form.totalCopies"
              required
          >
        </div>
        <div class="form-group">
          <label>状态</label>
          <select v-model="form.status">
            <option value="在馆">在馆</option>
            <option value="已借出">已借出</option>
          </select>
        </div>
        <div class="form-actions">
          <button type="button" class="cancel-btn" @click="closeDialog">取消</button>
          <button type="submit" class="submit-btn">保存修改</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    book: Object
  },
  data() {
    return {
      form: { ...this.book }
    };
  },
  methods: {
    closeDialog() {
      this.$emit('close');
    },

    submitForm() {
      // 验证数据
      if (parseInt(this.form.availableCopies) > parseInt(this.form.totalCopies)) {
        alert('可用副本数不能超过总副本数');
        return;
      }

      // 确保状态与可用副本一致
      if (this.form.availableCopies > 0 && this.form.status === '已借出') {
        if (!confirm('可用副本大于0但状态设为"已借出"，是否继续？')) return;
      }

      if (this.form.availableCopies === 0 && this.form.status === '在馆') {
        this.form.status = '已借出';
      }

      this.$emit('submit', this.form);
    },

    updateAvailableCopies() {
      if (this.form.availableCopies > this.form.totalCopies) {
        this.form.availableCopies = this.form.totalCopies;
      }
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
  z-index: 2000;
  backdrop-filter: blur(3px);
}

.dialog {
  position: relative;
  background: white;
  border-radius: 12px;
  padding: 30px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.3);
  z-index: 2001;
  animation: dialog-appear 0.3s ease-out;
}

@keyframes dialog-appear {
  0% {
    opacity: 0;
    transform: translateY(-20px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
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
  transition: color 0.2s;
}

.close-btn:hover {
  color: #e74c3c;
}

h2 {
  color: #d35400;
  text-align: center;
  margin-bottom: 1.5rem;
  font-size: 1.8rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #f39c12;
}

.form-group {
  margin-bottom: 1.2rem;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #2c3e50;
  font-size: 1rem;
}

input, select {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s;
  background-color: #f9f9f9;
}

input:focus, select:focus {
  border-color: #e67e22;
  box-shadow: 0 0 0 3px rgba(230, 126, 34, 0.2);
  outline: none;
  background-color: #fff;
}

.required {
  color: #e74c3c;
  margin-left: 4px;
}

.form-actions {
  display: flex;
  gap: 1rem;
  margin-top: 1.5rem;
}

.cancel-btn, .submit-btn {
  flex: 1;
  padding: 12px;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
}

.cancel-btn {
  background: #ecf0f1;
  color: #7f8c8d;
}

.cancel-btn:hover {
  background: #d0d3d4;
}

.submit-btn {
  background: linear-gradient(135deg, #f39c12, #e67e22);
  color: white;
  box-shadow: 0 4px 8px rgba(243, 156, 18, 0.3);
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(243, 156, 18, 0.4);
}
</style>