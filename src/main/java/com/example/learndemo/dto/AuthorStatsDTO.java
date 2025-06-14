package com.example.learndemo.dto;

import lombok.Data;

@Data
public class AuthorStatsDTO {
    private String author;
    private Long borrowCount;
} 