package com.example.learndemo.service;

import com.example.learndemo.domain.FinancialRecords;
import java.util.List;

public interface FinService {
    FinancialRecords depositOperation(String username, String operation, Double amount, String description);
    FinancialRecords prepaymentOperation(String username, String operation, Double amount, String description);
    FinancialRecords fineOperation(String username, String reason, Double amount, String description);
    List<FinancialRecords> getUserRecords(String username);
    List<FinancialRecords> getRecordsByType(String type);
}
