import request from '@/utils/request.js';

const statsApi = {
  getTop10Books() {
    return request({
      url: '/api/stats/books/top10',
      method: 'get'
    });
  },
  getTop10Authors() {
    return request({
      url: '/api/stats/authors/top10',
      method: 'get'
    });
  },
  getTop10Genres() {
    return request({
      url: '/api/stats/genres/top10',
      method: 'get'
    });
  },
  getTop10BooksByAuthor(author) {
    return request({
      url: `/api/stats/books/author/${encodeURIComponent(author)}`,
      method: 'get'
    });
  },
  getTop10BooksByGenre(genre) {
    return request({
      url: `/api/stats/books/genre/${encodeURIComponent(genre)}`,
      method: 'get'
    });
  }
};

export default statsApi;
