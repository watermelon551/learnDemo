<template>
  <div class="points-system">
    <div class="points-header">
      <h2>我的积分</h2>
      <button @click="refreshPoints" class="refresh-btn" :disabled="loading">刷新</button>
    </div>
    <div class="points-display">
      <div class="total-points">
        <span class="points-label">当前积分:</span>
        <span class="points-value">{{ points }}</span>
      </div>
      <div class="stars-display">
        <span class="stars-label">徽章等级:</span>
        <div class="stars">
          <img v-for="i in 5" :key="i"
               :src="getStarImage(i)"
               alt="star"
               class="star-icon">
        </div>
      </div>
    </div>
    <div class="points-details">
      <h3>积分详情</h3>
      <ul>
        <li>借阅书籍: +{{ summary.borrowedBooks }} 分 ({{ summary.borrowedBooks }} 本)</li>
        <li>按时归还: +{{ summary.returnedOnTime * 2 }} 分 ({{ summary.returnedOnTime }} 本)</li>
        <li>逾期未还: -{{ summary.overdueBooks }} 分 ({{ summary.overdueBooks }} 本)</li>
      </ul>
    </div>
    <div class="next-level">
      <p v-if="stars < 5">
        还需 {{ 30 - (points % 30) }} 分可升级到 {{ stars + 1 }} 星
      </p>
      <p v-else>
        恭喜您已达到最高等级!
      </p>
    </div>
  </div>
</template>

<script setup>
import pointsApi from '@/api/points.js';
import { ref } from 'vue';
import { useUserStore } from '@/stores/userStore';
import { ElMessage } from 'element-plus';
import starFilled from '@/assets/star-filled.png';
import starEmpty from '@/assets/star-empty.png';


const userStore = useUserStore();
const username = userStore.userInfo?.userName || userStore.userInfo?.username;
const points = ref(0);
const stars = ref(0);
const summary = ref({
  borrowedBooks: 0,
  returnedOnTime: 0,
  overdueBooks: 0,
  totalPoints: 0
});
const loading = ref(false);

const loadPoints = async () => {
  if (!username) return;
  loading.value = true;
  try {
    const [pointsRes, summaryRes] = await Promise.all([
      pointsApi.getMyPoints(username),
      pointsApi.getPointsSummary(username)
    ]);
    points.value = Number(pointsRes.data.points) || 0;
    stars.value = Number(pointsRes.data.level) || 0;
    summary.value = {
      borrowedBooks: Number(summaryRes.data?.borrowedBooks) || 0,
      returnedOnTime: Number(summaryRes.data?.returnedOnTime) || 0,
      overdueBooks: Number(summaryRes.data?.overdueBooks) || 0,
      totalPoints: Number(summaryRes.data?.totalPoints) || 0
    };
  } catch (e) {
    ElMessage.error('加载积分信息失败');
  } finally {
    loading.value = false;
  }
};

const refreshPoints = async () => {
  if (!username) return;
  loading.value = true;
  try {
    await pointsApi.updatePoints(username);
    await loadPoints();
    ElMessage.success('积分已刷新');
  } catch (e) {
    ElMessage.error('刷新积分失败');
  } finally {
    loading.value = false;
  }
};

function getStarImage(index) {
  if (index <= stars.value) {
    return starFilled;
  } else {
    return starEmpty;
  }
}

loadPoints();
</script>

<style scoped>
.points-system {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
.points-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.refresh-btn {
  padding: 5px 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.refresh-btn:disabled {
  background-color: #b2dfb2;
  cursor: not-allowed;
}
.refresh-btn:hover:enabled {
  background-color: #45a049;
}
.points-display {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}
.total-points, .stars-display {
  display: flex;
  align-items: center;
}
.points-label, .stars-label {
  font-weight: bold;
  margin-right: 10px;
}
.points-value {
  font-size: 24px;
  color: #4CAF50;
}
.stars {
  display: flex;
}
.star-icon {
  width: 24px;
  height: 24px;
  margin-right: 5px;
}
.points-details {
  margin-bottom: 20px;
}
.points-details h3 {
  margin-bottom: 10px;
}
.points-details ul {
  list-style-type: none;
  padding: 0;
}
.points-details li {
  margin-bottom: 5px;
}
.next-level {
  font-style: italic;
  color: #666;
}
</style>
