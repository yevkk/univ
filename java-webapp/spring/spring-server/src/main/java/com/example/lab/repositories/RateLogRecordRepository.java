package com.example.lab.repositories;

import com.example.lab.entities.book.changelogs.BalanceLogRecord;
import com.example.lab.entities.book.changelogs.RateLogRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RateLogRecordRepository extends JpaRepository<RateLogRecord, Long> {
    public List<RateLogRecord> findByBookId(long bookID);

    public List<RateLogRecord> findByDatetimeGreaterThanAndDatetimeLessThan(LocalDateTime periodStart, LocalDateTime periodEnd);
}
