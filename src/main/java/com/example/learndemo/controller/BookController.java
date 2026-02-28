package com.example.learndemo.controller;

import com.example.learndemo.domain.Book;
import com.example.learndemo.service.BookService;
import com.example.learndemo.domain.BorrowRecord;
import com.example.learndemo.mapper.BorrowRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.learndemo.mapper.UserMapper;
import com.example.learndemo.domain.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.example.learndemo.common.PlainResult;
import com.example.learndemo.service.PointsService;
import java.util.Collections;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PointsService pointsService;

    // 获取全部可借书籍
    @GetMapping("/books")
    public List<Book> getAllBooks(@RequestParam(value = "showAll", required = false) Boolean showAll) {
        if (Boolean.TRUE.equals(showAll)) {
            // 返回所有书籍（包括可借为0的）
            return bookService.findAllBooks();
        } else {
            // 只返回可借数量大于0的书籍
            return bookService.findAvailableBooks();
        }
    }

    // 根据书名精确查询
    @GetMapping("/books/title")
    public Book getBookByTitle(@RequestParam String title) {
        return bookService.findByTitle(title);
    }

    // 根据作者模糊查询
    @GetMapping("/books/author")
    public List<Book> getBooksByAuthor(@RequestParam String author) {
        return bookService.findByAuthorContainingIgnoreCase(author);
    }

    // 模糊搜索书名
    @GetMapping("/books/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        // 支持书名或作者模糊查询
        return bookService.searchBooksByTitleOrAuthor(keyword);
    }

    // 借书
    @PostMapping("/borrow/{id}")
    public String borrowBook(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 确保用户存在
        User user = userMapper.findOneByUsername(username);
        if (user == null) {
            return "借书失败：用户不存在";
        }

        boolean success = bookService.borrowBook(id);
        if (success) {
            // 保存借阅记录到 borrow_record 表
            BorrowRecord record = new BorrowRecord();
            record.setUserId(user.getId().intValue()); // 确保设置 user_id
            record.setBookId(id);
            record.setBorrowDate(LocalDate.now());
            record.setDueDate(LocalDate.now().plusDays(30)); // 默认30天

            // 使用枚举值替代字符串
            record.setStatus(BorrowRecord.BorrowStatus.ACTIVE);
            record.setType(BorrowRecord.BorrowType.借书);

            record.setCreatedAt(LocalDateTime.now());
            record.setUpdatedAt(LocalDateTime.now());

            // 打印日志检查记录内容
            System.out.println("创建借阅记录: " + record);

            borrowRecordMapper.insert(record);
            // 积分刷新
            pointsService.updateUserPoints(username);
            return "借书成功";
        } else {
            return "借书失败，库存不足或已借出";
        }
    }

    // 还书
    @PostMapping("/return/{bookId}")
    public String returnBook(@PathVariable("bookId") Integer bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 1. 获取当前用户信息
        User user = userMapper.findOneByUsername(username);
        if (user == null) {
            return "还书失败：用户不存在";
        }

        // 2. 使用条件构造器查询未归还的借阅记录
        QueryWrapper<BorrowRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId())
                .eq("book_id", bookId)
                .eq("status", BorrowRecord.BorrowStatus.ACTIVE.name()) // 使用枚举的name()
                .orderByDesc("borrow_date")
                .last("LIMIT 1");

        BorrowRecord record = borrowRecordMapper.selectOne(queryWrapper);

        if (record == null) {
            return "还书失败：未找到有效的借阅记录";
        }

        // 3. 执行还书操作
        boolean success = bookService.returnBook(bookId);
        if (success) {
            // 4. 更新借阅记录
            record.setReturnDate(LocalDate.now());
            record.setStatus(BorrowRecord.BorrowStatus.RETURNED);
            record.setUpdatedAt(LocalDateTime.now());
            borrowRecordMapper.updateById(record);
            // 积分刷新
            pointsService.updateUserPoints(username);
            return "还书成功，书籍已归还";
        } else {
            return "还书失败：库存更新失败";
        }
    }

    // ========== 借阅管理相关 ========== //

    @GetMapping("/borrow/records")
    public PlainResult<List<BorrowRecord>> getAllBorrowRecords() {
        return PlainResult.success(borrowRecordMapper.selectList(null));
    }

    @PostMapping("/borrow/renew/{id}")
    public boolean renewBookById(@PathVariable Integer id) {
        BorrowRecord record = borrowRecordMapper.selectById(id);
        if (record != null && record.getStatus() != null &&
                record.getStatus() != BorrowRecord.BorrowStatus.RETURNED) { // 使用枚举比较

            // 续借逻辑：应还日期顺延7天
            LocalDate due = record.getDueDate();
            if (due != null) {
                record.setDueDate(due.plusDays(7));
                borrowRecordMapper.updateById(record);
                return true;
            }
        }
        return false;
    }

    // 更新图书信息
    @PutMapping("/books/{id}")
    public PlainResult<String> updateBook(@PathVariable Integer id, @RequestBody Book book) {
        book.setBookId(id);
        boolean success = bookService.updateBook(book);
        if (success) {
            return PlainResult.success("图书信息更新成功");
        } else {
            return PlainResult.error(500, "图书信息更新失败");
        }
    }

    // 获取全部书籍（包括可借数为0的）
    @GetMapping("/books/all")
    public List<Book> getAllBooksWithZero() {
        return bookService.findAllBooks();
    }

    // 删除图书
    @DeleteMapping("/books/{id}")
    public PlainResult<String> deleteBook(@PathVariable Integer id) {
        boolean success = bookService.deleteBook(id);
        if (success) {
            return PlainResult.success("删除成功");
        } else {
            return PlainResult.error(500, "删除失败");
        }
    }

    // 查询当前用户所有未归还的借阅记录
    @GetMapping("/borrow/my")
    public List<com.example.learndemo.dto.BorrowRecordWithBookDto> getMyBorrowRecords() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userMapper.findOneByUsername(username);
        if (user == null) return Collections.emptyList();
        return borrowRecordMapper.selectActiveRecordsWithBookInfo(user.getId().intValue());
    }

    // 归还指定借阅记录
    @PostMapping("/borrow/return/{borrowId}")
    public String returnBookByBorrowId(@PathVariable Integer borrowId) {
        BorrowRecord record = borrowRecordMapper.selectById(borrowId);
        if (record == null || record.getStatus() != BorrowRecord.BorrowStatus.ACTIVE) {
            return "无效的借阅记录";
        }
        record.setStatus(BorrowRecord.BorrowStatus.RETURNED);
        record.setReturnDate(LocalDate.now());
        borrowRecordMapper.updateById(record);
        // 新增：归还后增加图书可借数
        bookService.returnBook(record.getBookId());
        return "还书成功";
    }

    // 添加新图书
    @PostMapping("/books/add")
    public PlainResult<String> addBook(@RequestBody Book book) {
        boolean success = bookService.addBook(book);
        if (success) {
            return PlainResult.success("添加成功");
        } else {
            return PlainResult.error(500, "添加失败");
        }
    }
}