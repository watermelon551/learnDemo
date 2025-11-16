<template>
  <div v-if="visible" class="dialog-overlay">
    <div class="dialog-content">
      <div class="message-list">
        <div v-for="(msg, index) in messages" :key="index"
             :class="['message', msg.role]">
          <span class="sender">{{ msg.role === 'user' ? '你' : '助手' }}：</span>
          <span class="content">{{ msg.content }}</span>
        </div>
        <div v-if="isLoading" class="message assistant">
          <span class="sender">助手：</span>
          <span class="content">思考中...</span>
        </div>
      </div>

      <div class="input-area">
        <textarea
          v-model="inputMessage"
          @keydown.enter.experimental="sendMessage"
          placeholder="输入关于图书管理的问题..."
          rows="2"
        ></textarea>
        <button @click="sendMessage" :disabled="isLoading">发送</button>
        <button @click="close">关闭</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {

  data() {

    return {

      visible: false,

      inputMessage: '',
      messages: [
        {
          role: 'assistant',
          content: '你好！你的图书助手。你可以问我任何问题。'
        }
      ],

      isLoading: false,


      apiKey: import.meta.env.DASHSCOPE_API_KEY || '', // 从环境变量获取 API 密钥
      apiUrl: 'https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation'
    };
  },
  methods: {
    open() {
      this.visible = true;
    },
    close() {
      this.visible = false;
    },
    async sendMessage() {
      if (!this.inputMessage.trim() || this.isLoading) return;

      // 检查 API 密钥

      if (!this.apiKey) {
        this.messages.push({
          role: 'assistant',
          content: '错误：连接异常。请检查API： DASHSCOPE_API_KEY。'
        });
        return;
      }

      // 添加用户消息
      const userMessage = this.inputMessage.trim();

      this.messages.push({
        role: 'user',
        content: userMessage
      });

      this.inputMessage = '';
      this.isLoading = true;

      try {
        // 创建系统提示词（根据你的图书管理系统定制）
        const systemPrompt = `你是一个专业的图书管理系统助手。系统功能包括：
          - 根据用户要求推荐图书
          请以专业、友好的方式回答用户关于图书管理的问题。`;

        // 构建消息历史（包含系统提示）
        const messagesForAPI = [
          { role: 'system', content: systemPrompt },
          ...this.messages.map(msg => ({
            role: msg.role,
            content: msg.content
          }))
        ];

        // 调用 DashScope API
        const response = await fetch(this.apiUrl, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${this.apiKey}`,
            'X-DashScope-SSE': 'disable'
          },
          body: JSON.stringify({
            model: 'qwen-turbo',
            input: {
              messages: messagesForAPI
            },
            parameters: {
              temperature: 0.7,
              max_tokens: 1000
            }
          })
        });

        if (!response.ok) {
          const errorData = await response.json().catch(() => ({}));
          throw new Error(`API 请求失败: ${response.status} ${response.statusText} ${errorData.message || ''}`);
        }

        const data = await response.json();


        if (data.output && data.output.text) {
          this.messages.push({
            role: 'assistant',
            content: data.output.text
          });
        } else {
          throw new Error('API 响应格式不正确');
        }
      } catch (error) {
        console.error('API调用错误:', error);
        this.messages.push({
          role: 'assistant',
          content: `抱歉，处理请求时出错。${error.message}`
        });
      } finally {
        this.isLoading = false;
        // 滚动到底部
        this.$nextTick(() => {
          const container = this.$el.querySelector('.message-list');
          if (container) container.scrollTop = container.scrollHeight;
        });
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
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.dialog-content {
  background: white;
  border-radius: 8px;
  width: 80%;
  max-width: 600px;
  max-height: 80vh;
  display: flex;
  flex-direction: column;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.message-list {
  padding: 20px;
  overflow-y: auto;
  flex-grow: 1;
  border-bottom: 1px solid #eee;
}

.message {
  margin-bottom: 15px;
  padding: 10px;
  border-radius: 8px;
}

.message.user {
  background-color: #e3f2fd;
  text-align: right;
}

.message.assistant {
  background-color: #f5f5f5;
  text-align: left;
}

.sender {
  font-weight: bold;
  margin-right: 5px;
}

.input-area {
  padding: 15px;
  display: flex;
  gap: 10px;
}

textarea {
  flex-grow: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: none;
  font-family: inherit;
}

button {
  padding: 8px 15px;
  background-color: #2196f3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:disabled {
  background-color: #90caf9;
  cursor: not-allowed;
}
</style>
