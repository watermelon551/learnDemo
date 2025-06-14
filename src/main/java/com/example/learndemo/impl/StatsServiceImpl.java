package com.example.learndemo.impl;

import com.example.learndemo.dto.BookStatsDTO;
import com.example.learndemo.dto.AuthorStatsDTO;
import com.example.learndemo.dto.GenreStatsDTO;
import com.example.learndemo.mapper.BorrowRecordMapper;
import com.example.learndemo.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {
    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

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
} 