package com.example.learndemo.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.learndemo.common.LibraryException;
import com.example.learndemo.domain.FinancialRecords;
import com.example.learndemo.domain.User;
import com.example.learndemo.mapper.FinancialRecordsMapper;
import com.example.learndemo.mapper.UserMapper;
import com.example.learndemo.service.FinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FinServiceImpl implements FinService {
    @Autowired
    private FinancialRecordsMapper financialRecordsMapper;
    
    @Autowired
    private UserMapper userMapper;

    private void checkUserExists(String username) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new LibraryException(400, "用户不存在：" + username);
        }
    }

    @Override
    @Transactional
    public FinancialRecords depositOperation(String username, String operation, Double amount, String description) {
        checkUserExists(username);
        if (amount <= 0) {
            throw new LibraryException(400, "金额必须大于0");
        }
        FinancialRecords record = new FinancialRecords();
        record.setUsername(username);
        record.setType("deposit");
        record.setOperation(operation);
        record.setAmount(BigDecimal.valueOf(amount));
        record.setDescription(description);
        record.setCreateTime(LocalDateTime.now());
        financialRecordsMapper.insert(record);
        return record;
    }

    @Override
    @Transactional
    public FinancialRecords prepaymentOperation(String username, String operation, Double amount, String description) {
        checkUserExists(username);
        if (amount == 0) {
            throw new LibraryException(400, "金额不能为0");
        }
        FinancialRecords record = new FinancialRecords();
        record.setUsername(username);
        record.setType("prepayment");
        record.setOperation(operation);
        record.setAmount(BigDecimal.valueOf(amount));
        record.setDescription(description);
        record.setCreateTime(LocalDateTime.now());
        financialRecordsMapper.insert(record);
        return record;
    }

    @Override
    @Transactional
    public FinancialRecords fineOperation(String username, String reason, Double amount, String description) {
        checkUserExists(username);
        if (amount <= 0) {
            throw new LibraryException(400, "金额必须大于0");
        }
        FinancialRecords record = new FinancialRecords();
        record.setUsername(username);
        record.setType("fine");
        record.setOperation("charge"); // 罚款操作固定为charge
        record.setAmount(BigDecimal.valueOf(amount));
        record.setDescription(description);
        record.setCreateTime(LocalDateTime.now());
        financialRecordsMapper.insert(record);
        return record;
    }

    @Override
    public List<FinancialRecords> getUserRecords(String username) {
        checkUserExists(username);
        return financialRecordsMapper.selectList(new QueryWrapper<FinancialRecords>().eq("username", username));
    }

    @Override
    public List<FinancialRecords> getRecordsByType(String type) {
        return financialRecordsMapper.selectList(new QueryWrapper<FinancialRecords>().eq("type", type));
    }

    @Override
    public double getUserPrepaymentBalance(String username) {
        checkUserExists(username);
        List<FinancialRecords> records = financialRecordsMapper.selectList(
            new QueryWrapper<FinancialRecords>().eq("username", username).eq("type", "prepayment")
        );
        return records.stream().mapToDouble(r -> r.getAmount().doubleValue()).sum();
    }
}
