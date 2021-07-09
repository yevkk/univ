package com.example.lab.services;

import com.example.lab.entities.book.Book;
import com.example.lab.entities.book.BookStats;
import com.example.lab.repositories.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lab.repositories.BookStatsRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookStatsRepository statsRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book find(long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    public void create(Book book) {
        bookRepository.save(book);
        statsRepository.save(new BookStats(book));
    }

    @Transactional
    public void update(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void delete(long id) {
        bookRepository.deleteById(id);
    }
}
