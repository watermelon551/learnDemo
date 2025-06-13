<template>

  <Header />
  <div class="container">
    <Aside />
    <RouterView />

  </div>
</template>


<script setup lang="ts">

import Header from "@/components/Header.vue";
</script>



<style scoped>
.container {

  display: flex;

}


</style>
