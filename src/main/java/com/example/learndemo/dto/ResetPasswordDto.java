package com.example.learndemo.dto;

import lombok.Data;

@Data
public class ResetPasswordDto {
    private String username;
    private String newPassword;
    private String authorize;
    private String phone;
} 