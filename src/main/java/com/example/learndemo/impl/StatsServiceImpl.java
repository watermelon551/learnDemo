package com.example.learndemo.impl;

import com.example.learndemo.dto.BookStatsDTO;
import com.example.learndemo.dto.AuthorStatsDTO;
import com.example.learndemo.dto.GenreStatsDTO;
import com.example.learndemo.mapper.BorrowRecordMapper;
import com.example.learndemo.mapper.UserMapper;
import com.example.learndemo.mapper.BookMapper;
import com.example.learndemo.mapper.FinancialRecordsMapper;
import com.example.learndemo.domain.FinancialRecords;
import com.example.learndemo.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StatsServiceImpl implements StatsService {
    @Autowired
    private BorrowRecordMapper borrowRecordMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private FinancialRecordsMapper financialRecordsMapper;

    @Override
    public List<BookStatsDTO> getTop10Books() {
        return borrowRecordMapper.selectTop10Books();
    }

    @Override
    public List<AuthorStatsDTO> getTop10Authors() {
        return borrowRecordMapper.selectTop10Authors();
    }

    @Override
    public List<GenreStatsDTO> getTop10Genres() {
        return borrowRecordMapper.selectTop10Genres();
    }

    @Override
    public List<BookStatsDTO> getTop10BooksByAuthor(String author) {
        return borrowRecordMapper.selectTop10BooksByAuthor(author);
    }

    @Override
    public List<BookStatsDTO> getTop10BooksByGenre(String genre) {
        return borrowRecordMapper.selectTop10BooksByGenre(genre);
    }

    @Override
    public long getTotalBorrowCount() {
        return borrowRecordMapper.selectCount(null);
    }

    @Override
    public List<Map<String, Object>> getBorrowTrend() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> raw = borrowRecordMapper.selectBorrowTrend();
        for (Map<String, Object> m : raw) {
            Map<String, Object> item = new HashMap<>();
            item.put("day", m.get("day"));
            item.put("count", m.get("count"));
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getUserTrend() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> raw = userMapper.selectUserTrend();
        for (Map<String, Object> m : raw) {
            Map<String, Object> item = new HashMap<>();
            item.put("day", m.get("day"));
            item.put("count", m.get("count"));
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getFinanceTrend() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> raw = financialRecordsMapper.selectFinanceTrend();
        for (Map<String, Object> m : raw) {
            Map<String, Object> item = new HashMap<>();
            item.put("day", m.get("day"));
            item.put("deposit", m.get("deposit"));
            item.put("rent", m.get("rent"));
            item.put("fine", m.get("fine"));
            result.add(item);
        }
        return result;
    }
} 