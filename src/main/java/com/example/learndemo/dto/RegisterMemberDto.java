package com.example.learndemo.dto;

import lombok.Data;

@Data
public class RegisterMemberDto {
    /**
     * 会员时长（月）
     */
    private Integer duration;

    /**
     * 用户名
     */
    private String username;
} 