package com.example.lab.repositories;

import com.example.lab.entities.book.changelogs.BalanceLogRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BalanceLogRecordRepository extends JpaRepository<BalanceLogRecord, Long> {
    public List<BalanceLogRecord> findByBookId(long bookID);

    public List<BalanceLogRecord> findByDatetimeGreaterThanAndDatetimeLessThan(LocalDateTime periodStart, LocalDateTime periodEnd);
}
