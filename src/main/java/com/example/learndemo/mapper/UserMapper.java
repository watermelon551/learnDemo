package com.example.learndemo.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.example.learndemo.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author ASUS
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-05-07 23:51:07
* @Entity com.example.learndemo.domain.User
*/
public interface UserMapper extends BaseMapper<User> {
    User findOneByUsername(@Param("username") String username);

}




