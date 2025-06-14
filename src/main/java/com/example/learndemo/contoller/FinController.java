package com.example.learndemo.contoller;

import com.example.learndemo.domain.FinancialRecords;
import com.example.learndemo.service.FinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/financial")
public class FinController {
    @Autowired
    private FinService finService;

    @PostMapping("/deposit")
    public ResponseEntity<?> depositOperation(@RequestParam String username,
                                             @RequestParam String operation,
                                             @RequestParam Double amount,
                                             @RequestParam String description) {
        try {
            FinancialRecords record = finService.depositOperation(username, operation, amount, description);
            return ResponseEntity.ok(record);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/prepayment")
    public ResponseEntity<?> prepaymentOperation(@RequestParam String username,
                                                @RequestParam String operation,
                                                @RequestParam Double amount,
                                                @RequestParam String description) {
        try {
            FinancialRecords record = finService.prepaymentOperation(username, operation, amount, description);
            return ResponseEntity.ok(record);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/fine")
    public ResponseEntity<?> fineOperation(@RequestParam String username,
                                          @RequestParam String reason,
                                          @RequestParam Double amount,
                                          @RequestParam String description) {
        try {
            FinancialRecords record = finService.fineOperation(username, reason, amount, description);
            return ResponseEntity.ok(record);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserRecords(@PathVariable String username) {
        try {
            List<FinancialRecords> records = finService.getUserRecords(username);
            return ResponseEntity.ok(records);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getRecordsByType(@PathVariable String type) {
        try {
            List<FinancialRecords> records = finService.getRecordsByType(type);
            return ResponseEntity.ok(records);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
