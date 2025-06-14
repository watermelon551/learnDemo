<script setup>
import { ref, onMounted } from 'vue';

const props = defineProps({
  width: {
    type: String,
    default: '120px'
  },
  height: {
    type: String,
    default: '40px'
  },
  length: {
    type: Number,
    default: 4
  }
});

const emit = defineEmits(['input']);

const codeList = ref([]);
const lines = ref([]);
const dots = ref([]);

const createCode = () => {
  const len = props.length;
  const newCodeList = [];
  const chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz0123456789';
  const charsLen = chars.length;

  // 生成验证码字符
  for (let i = 0; i < len; i++) {
    const rgb = [Math.round(Math.random() * 220), Math.round(Math.random() * 240), Math.round(Math.random() * 200)];
    newCodeList.push({
      code: chars.charAt(Math.floor(Math.random() * charsLen)),
      color: `rgb(${rgb})`,
      fontSize: `${16 + Math.floor(Math.random() * 8)}px`,
      padding: `${[Math.floor(Math.random() * 10)]}px`,
      transform: `rotate(${Math.floor(Math.random() * 30) - 15}deg)`
    });
  }

  // 生成干扰线
  const newLines = [];
  for (let i = 0; i < 4; i++) {
    const startX = Math.floor(Math.random() * parseInt(props.width));
    const startY = Math.floor(Math.random() * parseInt(props.height));
    const endX = Math.floor(Math.random() * parseInt(props.width));
    const endY = Math.floor(Math.random() * parseInt(props.height));
    const color = `rgba(${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, 0.3)`;
    newLines.push({ startX, startY, endX, endY, color });
  }

  // 生成干扰点
  const newDots = [];
  for (let i = 0; i < 50; i++) {
    const x = Math.floor(Math.random() * parseInt(props.width));
    const y = Math.floor(Math.random() * parseInt(props.height));
    const color = `rgba(${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, ${Math.floor(Math.random() * 255)}, 0.3)`;
    newDots.push({ x, y, color });
  }

  codeList.value = newCodeList;
  lines.value = newLines;
  dots.value = newDots;
  emit('input', newCodeList.map(item => item.code).join(''));
};

const refreshCode = () => {
  createCode();
};

const getStyle = (data) => {
  return `color: ${data.color}; font-size: ${data.fontSize}; padding: ${data.padding}; transform: ${data.transform}`;
};

onMounted(() => {
  createCode();
});
</script>

<template>
  <div
    class="validCode disabled-select"
    :style="`width:${width}; height:${height}`"
    @click="refreshCode"
  >
    <!-- 背景干扰点 -->
    <div class="dots-container">
      <div
        v-for="(dot, index) in dots"
        :key="'dot-' + index"
        class="dot"
        :style="{
          left: dot.x + 'px',
          top: dot.y + 'px',
          backgroundColor: dot.color
        }"
      ></div>
    </div>
    
    <!-- 干扰线 -->
    <svg class="lines-container">
      <line
        v-for="(line, index) in lines"
        :key="'line-' + index"
        :x1="line.startX"
        :y1="line.startY"
        :x2="line.endX"
        :y2="line.endY"
        :stroke="line.color"
        stroke-width="1"
      />
    </svg>

    <!-- 验证码字符 -->
    <span
      v-for="(item, index) in codeList"
      :key="index"
      :style="getStyle(item)"
      class="code-char"
    >{{ item.code }}</span>
  </div>
</template>

<style scoped>
.validCode {
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  position: relative;
  background-color: #f5f5f5;
  border-radius: 4px;
  overflow: hidden;
}

.code-char {
  display: inline-block;
  font-weight: bold;
  font-family: 'Arial', sans-serif;
  text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.1);
}

.dots-container {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.dot {
  position: absolute;
  width: 2px;
  height: 2px;
  border-radius: 50%;
}

.lines-container {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.disabled-select {
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
}
</style>
