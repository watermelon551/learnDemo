<template>
  <div class="layout-container">
    <Header class="header" />
    <div class="main-container">
      <Aside class="aside" />
      <main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import Header from "@/components/Header.vue";
import Aside from "@/components/Aside.vue";
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.main-container {
  display: flex;
  margin-top: 60px;
  min-height: calc(100vh - 60px);
}

.aside {
  position: fixed;
  left: 0;
  top: 60px;
  bottom: 0;
  z-index: 999;
  transition: all 0.3s ease;
}

.main-content {
  flex: 1;
  margin-left: 200px;
  padding: 20px;
  transition: all 0.3s ease;
  min-height: calc(100vh - 60px);
}

/* 路由切换动画 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s ease;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(20px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .aside {
    transform: translateX(-200px);
  }
  
  .main-content {
    margin-left: 0;
  }
  
  .aside.show {
    transform: translateX(0);
  }
  
  .main-content.shrink {
    margin-left: 200px;
  }
}
</style>
