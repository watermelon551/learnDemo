package com.example.learndemo.dto;

import lombok.Data;

@Data
public class BookStatsDTO {
    private Integer bookId;
    private String bookTitle;
    private String author;
    private String genre;
    private Long borrowCount;
} 