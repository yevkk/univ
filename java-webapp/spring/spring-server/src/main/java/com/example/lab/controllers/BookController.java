package com.example.lab.controllers;

import com.example.lab.entities.book.Book;
import com.example.lab.services.BookStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.lab.services.BookService;

@RestController
//@RequestMapping("/")
public class BookController {
    private final BookService bookService;
    private final BookStatsService statsService;

    public BookController(BookService bookService, BookStatsService statsService) {
        this.bookService = bookService;
        this.statsService = statsService;
    }

    @GetMapping("/")
    public ResponseEntity hello() {
        bookService.create(new Book(0, "book1", "auth1", "ua"));
        bookService.create(new Book(0, "book2", "auth2", "en"));
        var books = statsService.findAll();
        return ResponseEntity.ok(books);
    }

}
