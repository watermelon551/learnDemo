package com.example.learndemo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.learndemo.dto.*;

import java.util.List;

public interface UserService {
    void register(RegisterDto registerDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    void resetPassword(ResetPasswordDto resetPasswordDto);

    UserInfoDto getUserByUsername(String username);

    IPage<UserInfoDto> searchUser(Integer pageNum, Integer pageSize, String name, String phone);

    void updateUser(Long id, UserInfoDto userInfoDto);

    void deleteUser(Long id);

    void batchDelete(List<Long> ids);
}
