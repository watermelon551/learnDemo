package com.example.learndemo.impl;

import com.example.learndemo.common.LibraryException;
import com.example.learndemo.domain.User;
import com.example.learndemo.dto.*;
import com.example.learndemo.mapper.UserMapper;
import com.example.learndemo.service.UserService;
import com.example.learndemo.util.JwtUtil;
import jakarta.annotation.Resource;
import net.sf.jsqlparser.util.validation.metadata.NamedObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

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

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        // 验证管理员密码
        if (!"1234".equals(resetPasswordDto.getAuthorize())) {
            throw new LibraryException(400, "管理员验证码错误");
        }

        // 查找用户
        User user = userMapper.findOneByUsername(resetPasswordDto.getUsername());
        if (user == null) {
            throw new LibraryException(400, "用户不存在");
        }

        // 更新密码和更新时间
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }

    @Override
    public UserInfoDto getUserByUsername(String username) {
        User user = userMapper.findOneByUsername(username);
        if(user==null){
            return null;
        }
        return toUserInfoDto(user);

    }

    private UserInfoDto toUserInfoDto(User user) {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setNickName(user.getNickName());
        userInfoDto.setId(user.getId());
        userInfoDto.setUserName(user.getUsername());
        userInfoDto.setRole(user.getRole());
        return userInfoDto;
    }
}
