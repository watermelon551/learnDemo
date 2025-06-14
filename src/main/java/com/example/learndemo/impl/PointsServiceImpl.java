package com.example.learndemo.impl;

import com.example.learndemo.domain.UserPoints;
import com.example.learndemo.dto.UserPointsDto;
import com.example.learndemo.dto.PointsSummaryDto;
import com.example.learndemo.mapper.UserPointsMapper;
import com.example.learndemo.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Service
public class PointsServiceImpl implements PointsService {
    @Autowired
    private UserPointsMapper userPointsMapper;

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
        UserPoints up = userPointsMapper.selectOne(new QueryWrapper<UserPoints>().eq("username", username));
        if (up == null) return null;
        PointsSummaryDto dto = new PointsSummaryDto();
        dto.setUserId(username);
        dto.setTotalPoints(up.getPoints());
        dto.setRank(1); // TODO: 排名逻辑
        dto.setNextLevelPoints(up.getPoints() + 100); // TODO: 实际等级逻辑
        return dto;
    }

    @Override
    public void updateUserPoints(String username) {
        UserPoints up = userPointsMapper.selectOne(new QueryWrapper<UserPoints>().eq("username", username));
        if (up != null) {
            up.setPoints(up.getPoints() + 10); // 示例：每次调用加10分
            userPointsMapper.updateById(up);
        }
    }
} 