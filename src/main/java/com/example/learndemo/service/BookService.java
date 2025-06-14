package com.example.learndemo.service;

import com.example.learndemo.domain.Book;
import java.util.List;

public interface BookService {
    // 根据书名精确查询
    Book findByTitle(String title);

    // 根据作者模糊查询（忽略大小写）
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // 根据分类和状态查询
    List<Book> findByCategoryAndStatus(String category, String status);

    // 模糊搜索书名（使用LIKE）
    List<Book> searchByTitle(String keyword);

    // 查询可借阅的书籍
    List<Book> findAvailableBooks();

    // 借书操作（减少可借数量）
    boolean borrowBook(Integer bookId);

    // 还书操作（增加可借数量）
    boolean returnBook(Integer bookId);
}
