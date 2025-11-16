<template>
  <el-card class="trend-card" shadow="hover">
    <template #header>
      <div class="trend-title">财务收入趋势（按日）</div>
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
  const days = data.map(d => d.day);
  const deposit = data.map(d => d.deposit);
  const rent = data.map(d => d.rent);
  const fine = data.map(d => d.fine);
  chart.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['押金', '租金', '罚款'] },
    xAxis: { type: 'category', data: days },
    yAxis: { type: 'value' },
    series: [
      { name: '押金', type: 'line', data: deposit, smooth: true },
      { name: '租金', type: 'line', data: rent, smooth: true },
      { name: '罚款', type: 'line', data: fine, smooth: true }
    ],
    grid: { left: 40, right: 20, top: 40, bottom: 30 }
  });
};
onMounted(async () => {
  const res = await request.get('/api/stats/finance/trend');
  if (res.data) renderChart(res.data);
});
</script>
<style scoped>
.trend-card { border-radius: 14px; }
.trend-title { font-size: 1.1rem; font-weight: 600; color: #303133; }
</style>
