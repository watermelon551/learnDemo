<template>
  <el-card class="trend-card" shadow="hover">
    <template #header>
      <div class="trend-title">借阅趋势（按日）</div>
    </template>
    <div ref="chartRef" style="height:320px;width:100%"></div>
  </el-card>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import * as echarts from 'echarts';
import request from '@/utils/request';
const chartRef = ref();
const renderChart = (data) => {
  const chart = echarts.init(chartRef.value);
  const days = data.map(item => item.day);
  const counts = data.map(item => item.count);
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: days },
    yAxis: { type: 'value' },
    series: [{ name: '借阅量', type: 'line', data: counts, smooth: true, areaStyle: {} }],
    grid: { left: 40, right: 20, top: 40, bottom: 30 }
  });
};
onMounted(async () => {
  const res = await request.get('/api/stats/borrow/trend');
  if (res.data) renderChart(res.data);
});
</script>
<style scoped>
.trend-card { border-radius: 14px; }
.trend-title { font-size: 1.1rem; font-weight: 600; color: #303133; }
</style>
 