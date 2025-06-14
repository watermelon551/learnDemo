package com.example.learndemo.mapper;

import com.example.learndemo.domain.BorrowRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.learndemo.dto.BookStatsDTO;
import com.example.learndemo.dto.AuthorStatsDTO;
import com.example.learndemo.dto.GenreStatsDTO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* @author ASUS
* @description 针对表【borrow_record】的数据库操作Mapper
* @createDate 2025-06-14 18:30:29
* @Entity com.example.learndemo.domain.BorrowRecord
*/
public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {
    List<BookStatsDTO> selectTop10Books();
    List<AuthorStatsDTO> selectTop10Authors();
    List<GenreStatsDTO> selectTop10Genres();
    List<BookStatsDTO> selectTop10BooksByAuthor(@Param("author") String author);
    List<BookStatsDTO> selectTop10BooksByGenre(@Param("genre") String genre);
}




