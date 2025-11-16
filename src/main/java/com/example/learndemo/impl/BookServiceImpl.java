package com.example.learndemo.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.learndemo.domain.Book;
import com.example.learndemo.mapper.BookMapper;
import com.example.learndemo.service.BookService;
import com.example.learndemo.mapper.BorrowRecordMapper;
import com.example.learndemo.domain.BorrowRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    @Override
    public boolean addBook(Book book) {
        // 设置初始状态
        if (book.getAvailableCopies() == null) {
            book.setAvailableCopies(book.getTotalCopies());
        }
        if (book.getStatus() == null) {
            book.setStatus("可借阅");
        } else {
            // 确保状态值符合数据库限制
            String status = book.getStatus();
            if (status.length() > 10) {
                status = status.substring(0, 10);
            }
            book.setStatus(status);
        }
        return bookMapper.insert(book) > 0;
    }

    @Override
    public Book findByTitle(String title) {
        return bookMapper.selectOne(new QueryWrapper<Book>().eq("title", title));
    }

    @Override
    public List<Book> findByAuthorContainingIgnoreCase(String author) {
        return bookMapper.selectList(new QueryWrapper<Book>().like("author", author));
    }

    @Override
    public List<Book> findByCategoryAndStatus(String category, String status) {
        return bookMapper.selectList(new QueryWrapper<Book>().eq("category", category).eq("status", status));
    }

    @Override
    public List<Book> searchByTitle(String keyword) {
        return bookMapper.selectList(new QueryWrapper<Book>().like("title", keyword));
    }

    @Override
    public List<Book> findAvailableBooks() {
        return bookMapper.selectList(new QueryWrapper<Book>().gt("available_copies", 0));
    }

    @Override
    public List<Book> searchBooksByTitleOrAuthor(String keyword) {
        return bookMapper.selectList(new QueryWrapper<Book>()
            .like("title", keyword)
            .or()
            .like("author", keyword)
        );
    }

    @Override
    public boolean borrowBook(Integer bookId) {
        Book book = bookMapper.selectById(bookId);
        if (book != null && book.getAvailableCopies() > 0) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookMapper.updateById(book);
            return true;
        }
        return false;
    }

    @Override
    public boolean returnBook(Integer bookId) {
        Book book = bookMapper.selectById(bookId);
        if (book != null && book.getAvailableCopies() < book.getTotalCopies()) {
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookMapper.updateById(book);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateBook(Book book) {
        return bookMapper.updateById(book) > 0;
    }

    @Override
    public List<Book> findAllBooks() {
        return bookMapper.selectList(null);
    }

    @Override
    public boolean deleteBook(Integer bookId) {
        // 先删除所有关联的借阅记录
        QueryWrapper<BorrowRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("book_id", bookId);
        borrowRecordMapper.delete(wrapper);
        // 再删除图书
        return bookMapper.deleteById(bookId) > 0;
    }
}
