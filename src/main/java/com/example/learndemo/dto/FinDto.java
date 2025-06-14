package com.example.learndemo.dto;

import lombok.Data;

@Data
public class FinDto {
    private int id;
    private String username;
    private String type;
    private String amount;
    private String operation;
    private String description;
    private String createTime;
}
