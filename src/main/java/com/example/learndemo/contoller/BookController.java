package com.example.learndemo.contoller;

import com.example.learndemo.domain.Book;
import com.example.learndemo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // 获取全部可借书籍
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.findAvailableBooks();
    }

    // 根据书名精确查询
    @GetMapping("/title")
    public Book getBookByTitle(@RequestParam String title) {
        return bookService.findByTitle(title);
    }

    // 根据作者模糊查询
    @GetMapping("/author")
    public List<Book> getBooksByAuthor(@RequestParam String author) {
        return bookService.findByAuthorContainingIgnoreCase(author);
    }

    // 模糊搜索书名
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        return bookService.searchByTitle(keyword);
    }

    // 借书
    @PostMapping("/borrow/{id}")
    public boolean borrowBook(@PathVariable Integer id) {
        return bookService.borrowBook(id);
    }

    // 还书
    @PostMapping("/return/{id}")
    public boolean returnBook(@PathVariable Integer id) {
        return bookService.returnBook(id);
    }
}
