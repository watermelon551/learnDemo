package com.example.learndemo.dto;

import lombok.Data;

@Data
public class PointsSummaryDto {
    private String userId;
    private Integer totalPoints;
    private Integer rank;
    private Integer nextLevelPoints;
} 