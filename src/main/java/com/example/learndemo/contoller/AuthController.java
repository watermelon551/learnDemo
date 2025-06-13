package com.example.learndemo.contoller;

import com.example.learndemo.common.PlainResult;
import com.example.learndemo.dto.LoginRequestDto;
import com.example.learndemo.dto.LoginResponseDto;
import com.example.learndemo.dto.RegisterDto;
import com.example.learndemo.dto.ResetPasswordDto;
import com.example.learndemo.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public PlainResult<String> register(@RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
        return PlainResult.success("注册成功!");
    }

    @PostMapping("/login")
    public PlainResult<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = userService.login(loginRequestDto);
        return PlainResult.success(loginResponseDto);
    }

    @PostMapping("/reset-password")
    public PlainResult<String> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        userService.resetPassword(resetPasswordDto);
        return PlainResult.success("密码重置成功！");
    }
}
