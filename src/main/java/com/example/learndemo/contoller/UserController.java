package com.example.learndemo.contoller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.learndemo.common.PlainResult;
import com.example.learndemo.dto.UserInfoDto;
import com.example.learndemo.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping
    public PlainResult<UserInfoDto> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserInfoDto user = userService.getUserByUsername(username);
        if (user == null) {
            return PlainResult.error(404, "用户不存在");
        }
        return PlainResult.success(user);
    }
    @GetMapping("/search")
    public PlainResult<IPage<UserInfoDto>> searchUser(@RequestParam(required = false)Integer pageNum,
                                                      @RequestParam(required = false)Integer pageSize,
                                                      @RequestParam(required = false)String name,
                                                      @RequestParam(required = false)String phone){
        IPage<UserInfoDto> userInfoDtoIPage = userService.searchUser(pageNum,pageSize,name,phone);
        return PlainResult.success(userInfoDtoIPage);
    }
    @PutMapping("/{id}")
    public PlainResult<String> updateUser(@PathVariable Long id, @RequestBody UserInfoDto userInfoDto) {
        userService.updateUser(id, userInfoDto);
        return PlainResult.success("success");
    }
    @DeleteMapping("/{id}")
    public PlainResult<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return PlainResult.success("success");
    }

    @PostMapping("/deleteBatch")
    public PlainResult<String> deleteBatch(@RequestBody List<Long> ids) {
        userService.batchDelete(ids);
        return PlainResult.success("success");
    }



}
