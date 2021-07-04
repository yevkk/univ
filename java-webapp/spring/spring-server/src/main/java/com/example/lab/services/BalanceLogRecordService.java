package com.example.lab.services;

import com.example.lab.entities.book.changelogs.BalanceLogRecord;
import com.example.lab.repositories.BalanceLogRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BalanceLogRecordService {
    private final BalanceLogRecordRepository repository;

    public List<BalanceLogRecord> findAll() {
        return repository.findAll();
    }

    public List<BalanceLogRecord> findByBookId(long bookId) {
        return repository.findAllByBookId(bookId);
    }

    public List<BalanceLogRecord> findInPeriod(LocalDateTime periodStart, LocalDateTime periodEnd) {
        return repository.findAllByDatetimeGreaterThanAndDatetimeLessThan(periodStart, periodEnd);
    }
}
