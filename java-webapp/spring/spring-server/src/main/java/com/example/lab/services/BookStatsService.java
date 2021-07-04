package com.example.lab.services;

import com.example.lab.entities.book.BookStats;
import com.example.lab.entities.book.changelogs.BalanceLogRecord;
import com.example.lab.repositories.BalanceLogRecordRepository;
import com.example.lab.repositories.BookRepository;
import com.example.lab.repositories.BookStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookStatsService {
    private final BookStatsRepository statsRepository;
    private final BookRepository bookRepository;
    private final BalanceLogRecordRepository balanceLogRepository;
    
    public BookStatsService(BookStatsRepository statsRepository, BookRepository bookRepository, BalanceLogRecordRepository balanceLogRepository) {
        this.statsRepository = statsRepository;
        this.bookRepository = bookRepository;
        this.balanceLogRepository = balanceLogRepository;
    }

    public List<BookStats> findAll() {
        return statsRepository.findAll();
    }

    public BookStats findByBookId(long bookId) {
        return statsRepository.findByBookId(bookId);
    }

    @Transactional
    public void update(long bookId, int newAmount, String comment) {
        var stats = statsRepository.findByBookId(bookId);
        var book = bookRepository.findById(bookId).orElse(null);
        var diff = newAmount - stats.getAmount();

        stats.setAmount(newAmount);
        statsRepository.save(stats);
        balanceLogRepository.save(new BalanceLogRecord(0, LocalDateTime.now(), book, diff, comment));
    }

}
