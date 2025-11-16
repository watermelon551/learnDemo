package com.example.learndemo.mapper;

import com.example.learndemo.domain.FinancialRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
* @author ASUS
* @description 针对表【financial_records】的数据库操作Mapper
* @createDate 2025-06-14 23:16:52
* @Entity com.example.learndemo.domain.FinancialRecords
*/
public interface FinancialRecordsMapper extends BaseMapper<FinancialRecords> {

    // 按月统计押金/租金/罚款收入
    List<Map<String, Object>> selectFinanceTrend();

}




