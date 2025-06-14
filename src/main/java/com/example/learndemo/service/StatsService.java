package com.example.learndemo.service;

import com.example.learndemo.dto.BookStatsDTO;
import com.example.learndemo.dto.AuthorStatsDTO;
import com.example.learndemo.dto.GenreStatsDTO;
import java.util.List;

public interface StatsService {
    List<BookStatsDTO> getTop10Books();
    List<AuthorStatsDTO> getTop10Authors();
    List<GenreStatsDTO> getTop10Genres();
    List<BookStatsDTO> getTop10BooksByAuthor(String author);
    List<BookStatsDTO> getTop10BooksByGenre(String genre);
} 