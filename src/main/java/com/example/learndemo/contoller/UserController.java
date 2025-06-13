package com.example.learndemo.contoller;


import com.example.learndemo.common.PlainResult;
import com.example.learndemo.dto.UserInfoDto;
import com.example.learndemo.service.UserService;
import com.example.learndemo.util.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserService userService;

    @GetMapping
    public PlainResult<UserInfoDto> getUserInfo(@RequestParam String token){
        String username = jwtUtil.extractUsername(token);
        UserInfoDto user = userService.getUserByUsername(username);
        return PlainResult.success(user);

    }



}
