package com.example.learndemo.dto;

import lombok.Data;

@Data
public class UserPointsDto {
    private String userId;
    private Integer points;
    private String level;
} 