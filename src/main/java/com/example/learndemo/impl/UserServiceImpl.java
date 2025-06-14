package com.example.learndemo.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

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
        try {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
            
        LoginResponseDto loginResponseDto = new LoginResponseDto();
            String token = jwtUtil.generateToken(loginRequestDto.getUsername());
            loginResponseDto.setToken(token);
            
            return loginResponseDto;
        } catch (Exception e) {
            throw new LibraryException(401, "用户名或密码错误");
        }
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

        // 验证手机号码
        if (user.getPhone() == null || !user.getPhone().equals(resetPasswordDto.getPhone())) {
            throw new LibraryException(400, "手机号码与注册时不一致");
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

    @Override
    public IPage<UserInfoDto> searchUser(Integer pageNum, Integer pageSize, String name, String phone) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        LambdaQueryWrapper<User> wrappers = Wrappers.lambdaQuery();
        wrappers.orderByDesc(User::getUpdatedAt);
        if (StringUtils.hasText(name)) {
            wrappers.like(User::getNickName, name);
        }
        if (StringUtils.hasText(phone)) {
            wrappers.like(User::getPhone, phone);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrappers);
        if (userPage == null) {
            return null;
        }
        return userPage.convert(this::toUserInfoDto);
    }

    @Override
    public void updateUser(Long id, UserInfoDto userInfoDto) {
        User user = toUser(id, userInfoDto);
        userMapper.updateById(user);
    }

    private User toUser(Long id, UserInfoDto userInfoDto) {
        User user = new User();
        user.setId(id);
        user.setUsername(userInfoDto.getUserName());
        user.setNickName(userInfoDto.getNickName());
        user.setPhone(userInfoDto.getPhone());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        userMapper.deleteBatchIds(ids);

    }

    private UserInfoDto toUserInfoDto(User user) {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setNickName(user.getNickName());
        userInfoDto.setId(user.getId());
        userInfoDto.setUserName(user.getUsername());
        userInfoDto.setRole(user.getRole());
        userInfoDto.setPhone(user.getPhone());
        return userInfoDto;
    }
}
