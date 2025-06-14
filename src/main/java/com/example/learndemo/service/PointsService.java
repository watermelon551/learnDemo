package com.example.learndemo.service;

import com.example.learndemo.dto.UserPointsDto;
import com.example.learndemo.dto.PointsSummaryDto;

public interface PointsService {
    UserPointsDto getUserPoints(String username);
    PointsSummaryDto calculatePointsSummary(String username);
    void updateUserPoints(String username);
}
