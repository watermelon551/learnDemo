<template>
  <div>
    <router-view/>
    <ChatAiIcon @openDialog="openDialog" />
    <DeepSeekDialog ref="deepSeekDialog" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ChatAiIcon from './components/ChatAiIcon.vue'
import DeepSeekDialog from './components/DeepSeekDialog.vue'

const deepSeekDialog = ref(null)

const openDialog = () => {
  deepSeekDialog.value.open()
}
</script>





