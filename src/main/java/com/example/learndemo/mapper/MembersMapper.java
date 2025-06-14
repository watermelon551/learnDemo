package com.example.learndemo.mapper;

import com.example.learndemo.domain.Members;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.learndemo.domain.User;
import org.apache.ibatis.annotations.Param;

/**
* @author ASUS
* @description 针对表【members】的数据库操作Mapper
* @createDate 2025-05-07 23:51:07
* @Entity com.example.learndemo.domain.Members
*/
public interface MembersMapper extends BaseMapper<Members> {
    User findOneByUserid(@Param("user_id") String user_id);
}




