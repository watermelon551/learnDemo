package com.example.learndemo.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String username;
    //地址


    private String password;
    private String role;
    private String phone;
    private String nickName;

}
