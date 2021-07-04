package com.example.lab.services;

import com.example.lab.entities.book.changelogs.BalanceLogRecord;
import com.example.lab.entities.book.request.BookRequestGet;
import com.example.lab.entities.book.request.BookRequestReturn;
import com.example.lab.entities.book.request.misc.RequestState;
import com.example.lab.repositories.BalanceLogRecordRepository;
import com.example.lab.repositories.BookRequestGetRepository;
import com.example.lab.repositories.BookRequestReturnRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BookRequestReturnService {
    private final BookRequestReturnRepository requestReturnRepository;
    private final BookRequestGetRepository requestGetRepository;
    private final BalanceLogRecordRepository balanceLogRepository;

    public List<BookRequestReturn> findAll() {
        return requestReturnRepository.findAll();
    }

    public List<BookRequestReturn> findByUserId(long userId) {
        return requestReturnRepository.findAllByRequestUserID(userId);
    }

    @Transactional
    public void create(BookRequestReturn returnRequest) {
        requestReturnRepository.save(returnRequest);
    }

    @Transactional
    public void update(long id, RequestState state) {
        var returnRequest = requestReturnRepository.getById(id);
        returnRequest.setState(state);
        requestReturnRepository.save(returnRequest);

        if (state == RequestState.PROCESSED) {
            returnRequest.getRequest().setState(RequestState.RETURNED);
            requestGetRepository.save(returnRequest.getRequest());
            balanceLogRepository.save(new BalanceLogRecord(0, LocalDateTime.now(), returnRequest.getRequest().getBook(), 1, "returned"));
        }
    }
}
