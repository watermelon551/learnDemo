package com.example.learndemo.contoller;

import com.example.learndemo.dto.BookStatsDTO;
import com.example.learndemo.dto.AuthorStatsDTO;
import com.example.learndemo.dto.GenreStatsDTO;
import com.example.learndemo.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    @Autowired
    private StatsService statsService;

    @GetMapping("/books/top10")
    public List<BookStatsDTO> getTop10Books() {
        return statsService.getTop10Books();
    }

    @GetMapping("/authors/top10")
    public List<AuthorStatsDTO> getTop10Authors() {
        return statsService.getTop10Authors();
    }

    @GetMapping("/genres/top10")
    public List<GenreStatsDTO> getTop10Genres() {
        return statsService.getTop10Genres();
    }

    @GetMapping("/books/author/{author}")
    public List<BookStatsDTO> getTop10BooksByAuthor(@PathVariable String author) {
        return statsService.getTop10BooksByAuthor(author);
    }

    @GetMapping("/books/genre/{genre}")
    public List<BookStatsDTO> getTop10BooksByGenre(@PathVariable String genre) {
        return statsService.getTop10BooksByGenre(genre);
    }
}
