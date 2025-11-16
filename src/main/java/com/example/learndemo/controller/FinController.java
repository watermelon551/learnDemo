package com.example.learndemo.controller;

import com.example.learndemo.common.LibraryException;
import com.example.learndemo.common.PlainResult;
import com.example.learndemo.domain.FinancialRecords;
import com.example.learndemo.service.FinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/financial")
public class FinController {
    @Autowired
    private FinService finService;

    @PostMapping("/deposit")
    public PlainResult<FinancialRecords> depositOperation(@RequestParam String username,
                                                         @RequestParam String operation,
                                                         @RequestParam Double amount,
                                                         @RequestParam String description) {
        try {
            FinancialRecords record = finService.depositOperation(username, operation, amount, description);
            return PlainResult.success(record);
        } catch (LibraryException e) {
            return PlainResult.error(e.getCode(), e.getMessage());
        }
    }

    @PostMapping("/prepayment")
    public PlainResult<FinancialRecords> prepaymentOperation(@RequestParam String username,
                                                            @RequestParam String operation,
                                                            @RequestParam Double amount,
                                                            @RequestParam String description) {
        try {
            FinancialRecords record = finService.prepaymentOperation(username, operation, amount, description);
            return PlainResult.success(record);
        } catch (LibraryException e) {
            return PlainResult.error(e.getCode(), e.getMessage());
        }
    }

    @PostMapping("/fine")
    public PlainResult<FinancialRecords> fineOperation(@RequestParam String username,
                                                      @RequestParam String reason,
                                                      @RequestParam Double amount,
                                                      @RequestParam String description) {
        try {
            FinancialRecords record = finService.fineOperation(username, reason, amount, description);
            return PlainResult.success(record);
        } catch (LibraryException e) {
            return PlainResult.error(e.getCode(), e.getMessage());
        }
    }

    @GetMapping("/user/{username}")
    public PlainResult<List<FinancialRecords>> getUserRecords(@PathVariable String username) {
        try {
            List<FinancialRecords> records = finService.getUserRecords(username);
            return PlainResult.success(records);
        } catch (LibraryException e) {
            return PlainResult.error(e.getCode(), e.getMessage());
        }
    }

    @GetMapping("/type/{type}")
    public PlainResult<List<FinancialRecords>> getRecordsByType(@PathVariable String type) {
        try {
            List<FinancialRecords> records = finService.getRecordsByType(type);
            return PlainResult.success(records);
        } catch (LibraryException e) {
            return PlainResult.error(e.getCode(), e.getMessage());
        }
    }

    @GetMapping("/prepayment/balance/{username}")
    public PlainResult<Double> getUserPrepaymentBalance(@PathVariable String username) {
        try {
            double balance = finService.getUserPrepaymentBalance(username);
            return PlainResult.success(balance);
        } catch (LibraryException e) {
            return PlainResult.error(e.getCode(), e.getMessage());
        }
    }
}
