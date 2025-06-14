package com.example.learndemo.contoller;

import com.example.learndemo.dto.UserPointsDto;
import com.example.learndemo.dto.PointsSummaryDto;
import com.example.learndemo.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/points")
public class PointsController {
    @Autowired
    private PointsService pointsService;

    @GetMapping("/my")
    public UserPointsDto getMyPoints(@RequestParam String userId) {
        return pointsService.getUserPoints(userId);
    }

    @GetMapping("/summary")
    public PointsSummaryDto getPointsSummary(@RequestParam String userId) {
        return pointsService.calculatePointsSummary(userId);
    }

    @PostMapping("/update")
    public void updatePoints(@RequestParam String userId) {
        pointsService.updateUserPoints(userId);
    }
}
