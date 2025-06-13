package com.example.learndemo.service;

import com.example.learndemo.dto.*;

public interface UserService {
    void register(RegisterDto registerDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    void resetPassword(ResetPasswordDto resetPasswordDto);

    UserInfoDto getUserByUsername(String username);
}
