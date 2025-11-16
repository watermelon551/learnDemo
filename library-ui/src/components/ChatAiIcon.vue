<template>
  <div class="ai-chat-icon" @click="handleClick">
    <svg class="icon" aria-hidden="true">
      <use xlink:href="#iconperson"></use>
    </svg>
  </div>
</template>

<script setup>
const emit = defineEmits(['openDialog'])
function handleClick() {
  emit('openDialog')
}
</script>

<style scoped>
.ai-chat-icon {
  position: fixed;
  right: 24px;
  bottom: 24px;
  z-index: 2000;
  background: rgba(255, 255, 255, 0.9);
  color: #666;
  border-radius: 50%;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(4px);
}

.ai-chat-icon:hover {
  background: rgba(255, 255, 255, 1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.icon {
  font-size: 24px;
  width: 24px;
  height: 24px;
  fill: #666;
  transition: fill 0.3s ease;
}

.ai-chat-icon:hover .icon {
  fill: #333;
}
</style> 