package com.example.learndemo.dto;

import lombok.Data;
@Data

public class BookDto {
    private int id;
    private String title;
    private String author;
    private String category;
    private String publisher;
    private String published_year;
    private String isbn;
    private int total_copies;
    private int available_copies;
    private boolean status;
}
