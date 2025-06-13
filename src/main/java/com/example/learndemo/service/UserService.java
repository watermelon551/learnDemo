package com.example.learndemo.service;

import com.example.learndemo.dto.LoginRequestDto;
import com.example.learndemo.dto.LoginResponseDto;
import com.example.learndemo.dto.RegisterDto;

public interface UserService {
    void register(RegisterDto registerDto);


    LoginResponseDto login(LoginRequestDto loginRequestDto) ;


}
