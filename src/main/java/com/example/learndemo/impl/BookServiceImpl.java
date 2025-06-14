package com.example.learndemo.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.learndemo.domain.Book;
import com.example.learndemo.mapper.BookMapper;
import com.example.learndemo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

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
}
