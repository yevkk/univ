package com.example.lab.controllers;

import com.example.lab.entities.book.changelogs.BalanceLogRecord;
import com.example.lab.services.BalanceLogRecordService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/balance_log")
@CrossOrigin(originPatterns = "*")
@AllArgsConstructor
public class BalanceLogRecordController {
    private final BalanceLogRecordService balanceLogService;

    @GetMapping("")
    @RolesAllowed("library-admin")
    public ResponseEntity get(@RequestParam(value = "book_id", required = false) Long bookId,
                              @RequestParam(value = "from", required = false) String periodStart,
                              @RequestParam(value = "to", required = false) String periodEnd) {
        List<BalanceLogRecord> log;

        if (bookId != null) {
            log = balanceLogService.findByBookId(bookId);
        } else if (periodStart != null && periodEnd != null) {
            log = balanceLogService.findInPeriod(LocalDateTime.parse(periodStart), LocalDateTime.parse(periodEnd));
        } else {
            log = balanceLogService.findAll();
        }

        return ResponseEntity.ok(log);
    }
}
