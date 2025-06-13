package com.example.learndemo.impl;

import com.example.learndemo.common.LibraryException;
import com.example.learndemo.domain.User;
import com.example.learndemo.dto.LoginRequestDto;
import com.example.learndemo.dto.LoginResponseDto;
import com.example.learndemo.dto.RegisterDto;
import com.example.learndemo.mapper.UserMapper;
import com.example.learndemo.service.UserService;
import com.example.learndemo.util.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;


    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtUtil jwtUtil;



    @Override
    public void register(RegisterDto registerDto) {
         User originuser = userMapper.findOneByUsername(registerDto.getUsername());
         if (originuser != null) {
              throw new LibraryException(400,"用户名已存在");
         }
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(registerDto.getRole());
        user.setPhone(registerDto.getPhone());
        user.setNickName(registerDto.getNickName());
        userMapper.insert(user);

    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setToken(jwtUtil.generateToken(loginRequestDto.getUsername()));




        return null;
    }
}
