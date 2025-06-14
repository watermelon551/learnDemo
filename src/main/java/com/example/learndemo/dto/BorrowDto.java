package com.example.learndemo.dto;

import lombok.Data;

@Data
public class BorrowDto {
    private int id;
    private int userId;
    private int bookId;
    private String borrowDate;
    private String dueDate;
    private String returnDate;
    private String type;
    private String status;

}
