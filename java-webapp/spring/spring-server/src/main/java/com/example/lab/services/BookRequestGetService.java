package com.example.lab.services;

import com.example.lab.entities.book.changelogs.BalanceLogRecord;
import com.example.lab.entities.book.request.BookRequestGet;
import com.example.lab.entities.book.request.misc.RequestState;
import com.example.lab.repositories.BalanceLogRecordRepository;
import com.example.lab.repositories.BookRequestGetRepository;
import com.example.lab.repositories.BookStatsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BookRequestGetService {
    private final BookRequestGetRepository requestGetRepository;
    private final BalanceLogRecordRepository balanceLogRepository;
    private final BookStatsRepository statsRepository;

    public List<BookRequestGet> findAll() {
        return requestGetRepository.findAll();
    }

    public BookRequestGet findById(long id) {
        return requestGetRepository.getById(id);
    }

    public List<BookRequestGet> findByUserId(long userId) {
        return requestGetRepository.findAllByUserID(userId);
    }

    @Transactional
    public void create(BookRequestGet request) {
        requestGetRepository.save(request);
    }

    @Transactional
    public void update(long id, RequestState state) {
        var request = requestGetRepository.getById(id);
        request.setState(state);
        requestGetRepository.save(request);

        if (state == RequestState.PROCESSED) {
            balanceLogRepository.save(new BalanceLogRecord(0, LocalDateTime.now(), request.getBook(), -1, "requested"));

            var stats = statsRepository.findByBookId(request.getBook().getId());
            stats.setTotalRequests(stats.getTotalRequests() + 1);
            statsRepository.save(stats);
        }
    }
}
