import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api/stats',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json'
  }
});

export default {
  getTop10Books() {
    return apiClient.get('/books/top10');  // 使用已定义的apiClient
  },
  getTop10Authors() {
    return apiClient.get('/authors/top10');  // 使用apiClient代替axios和API_URL
  },
  getTop10Genres() {
    return apiClient.get('/genres/top10');
  },
  getTop10BooksByAuthor(author) {
    return apiClient.get(`/books/author/${encodeURIComponent(author)}`);
  },
  getTop10BooksByGenre(genre) {
    return apiClient.get(`/books/genre/${encodeURIComponent(genre)}`);
  }
};
