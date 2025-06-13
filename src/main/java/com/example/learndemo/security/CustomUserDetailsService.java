package com.example.learndemo.security;

import com.example.learndemo.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 自定义用户认证服务
 * 实现Spring Security的UserDetailsService接口，用于加载用户特定的数据
 * 主要职责是将我们的用户实体转换为Spring Security可以理解的UserDetails对象
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {
    
    @Resource
    private UserMapper userMapper;

    /**
     * 根据用户名加载用户信息
     * 这个方法会被Spring Security在认证过程中调用
     * 
     * @param username 用户名
     * @return UserDetails 包含用户认证和授权信息的对象
     * @throws UsernameNotFoundException 当用户不存在时抛出此异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库查询用户信息
        com.example.learndemo.domain.User user = userMapper.findOneByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found with username" + username);
        }
        // 将我们的用户实体转换为Spring Security的User对象
        // 第三个参数是用户的权限列表，这里暂时为空列表
        return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
