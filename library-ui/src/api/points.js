import request from "@/utils/request.js";

const pointsApi = {
  getMyPoints(username) {
    return request({
      url: `/api/points/my`,
      method: 'get',
      params: { userId: username }
    });
  },
  getPointsSummary(username) {
    return request({
      url: `/api/points/summary`,
      method: 'get',
      params: { userId: username }
    });
  },
  updatePoints(username) {
    return request({
      url: `/api/points/update`,
      method: 'post',
      params: { userId: username }
    });
  }
};

export default pointsApi;
