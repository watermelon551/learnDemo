package com.example.learndemo.dto;
import lombok.Data;
import java.time.LocalDate;

@Data
public class BorrowRecordWithBookDto {
    private Integer id;
    private Integer bookId;
    private String bookTitle;
    private String author;
    private LocalDate borrowDate;
    private LocalDate dueDate;
} 