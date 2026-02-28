package com.example.learndemo.impl;

import com.example.learndemo.domain.UserPoints;
import com.example.learndemo.dto.UserPointsDto;
import com.example.learndemo.dto.PointsSummaryDto;
import com.example.learndemo.mapper.UserPointsMapper;
import com.example.learndemo.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.learndemo.mapper.BorrowRecordMapper;
import com.example.learndemo.mapper.UserMapper;
import com.example.learndemo.domain.User;

@Service
public class PointsServiceImpl implements PointsService {
    @Autowired
    private UserPointsMapper userPointsMapper;
    @Autowired
    private BorrowRecordMapper borrowRecordMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserPointsDto getUserPoints(String username) {
        UserPoints up = userPointsMapper.selectOne(new QueryWrapper<UserPoints>().eq("username", username));
        if (up == null) return null;
        UserPointsDto dto = new UserPointsDto();
        dto.setUserId(username);
        dto.setPoints(up.getPoints());
        dto.setLevel(up.getStars() != null ? up.getStars().toString() : "0");
        return dto;
    }

    @Override
    public PointsSummaryDto calculatePointsSummary(String username) {
        User user = userMapper.findOneByUsername(username);
        if (user == null) return null;
        Integer userId = user.getId().intValue();
        int borrowedBooks = borrowRecordMapper.countBorrowedBooks(userId);
        int returnedOnTime = borrowRecordMapper.countReturnedOnTime(userId);
        int overdueBooks = borrowRecordMapper.countOverdueBooks(userId);
        int totalPoints = borrowedBooks + returnedOnTime * 2 - overdueBooks;
        if (totalPoints < 0) totalPoints = 0;
        PointsSummaryDto dto = new PointsSummaryDto();
        dto.setUserId(username);
        dto.setTotalPoints(totalPoints);
        dto.setRank(1); // 可扩展
        dto.setNextLevelPoints(30 - (totalPoints % 30));
        dto.setBorrowedBooks(borrowedBooks);
        dto.setReturnedOnTime(returnedOnTime);
        dto.setOverdueBooks(overdueBooks);
        return dto;
    }

    @Override
    public void updateUserPoints(String username) {
        User user = userMapper.findOneByUsername(username);
        if (user == null) return;
        Integer userId = user.getId().intValue();
        int borrowedBooks = borrowRecordMapper.countBorrowedBooks(userId);
        int returnedOnTime = borrowRecordMapper.countReturnedOnTime(userId);
        int overdueBooks = borrowRecordMapper.countOverdueBooks(userId);
        int totalPoints = borrowedBooks + returnedOnTime * 2 - overdueBooks;
        if (totalPoints < 0) totalPoints = 0;
        int stars = Math.min(5, totalPoints / 30 + (totalPoints % 30 > 0 ? 1 : 0));
        UserPoints up = userPointsMapper.selectOne(new QueryWrapper<UserPoints>().eq("username", username));
        if (up == null) {
            up = new UserPoints();
            up.setUsername(username);
            up.setPoints(totalPoints);
            up.setStars(stars);
            userPointsMapper.insert(up);
        } else {
            up.setPoints(totalPoints);
            up.setStars(stars);
            userPointsMapper.updateById(up);
        }
    }
} 