package com.example.learndemo.mapper;

import com.example.learndemo.domain.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* @author ASUS
* @description 针对表【book】的数据库操作Mapper
* @createDate 2025-06-14 18:30:29
* @Entity com.example.learndemo.domain.Book
*/
public interface BookMapper extends BaseMapper<Book> {

    List<Book> findAvailableBooks();
    List<Book> searchBooksByTitleOrAuthor(String keyword);

}




