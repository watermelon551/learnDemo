package com.example.learndemo.mapper;

import com.example.learndemo.domain.BorrowRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.learndemo.dto.BookStatsDTO;
import com.example.learndemo.dto.AuthorStatsDTO;
import com.example.learndemo.dto.GenreStatsDTO;
import org.apache.ibatis.annotations.Select; // 添加这个导入
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

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

    // 统计某用户借阅总数
    int countBorrowedBooks(@Param("userId") Integer userId);

    // 统计某用户按时归还的书籍数（归还日期<=应还日期）
    int countReturnedOnTime(@Param("userId") Integer userId);

    // 统计某用户逾期未还的书籍数（归还日期>应还日期）
    int countOverdueBooks(@Param("userId") Integer userId);

    // 按月统计借阅量
    List<Map<String, Object>> selectBorrowTrend();

    /**
     * 查询指定用户的未归还借阅记录，联表返回书名和作者
     */
    @Select("SELECT br.id, br.book_id, b.title AS book_title, b.author, br.borrow_date, br.due_date " +
            "FROM borrow_record br " +
            "JOIN book b ON br.book_id = b.book_id " +
            "WHERE br.user_id = #{userId} AND br.status = 'ACTIVE'")
    List<com.example.learndemo.dto.BorrowRecordWithBookDto> selectActiveRecordsWithBookInfo(@Param("userId") Integer userId);
}




