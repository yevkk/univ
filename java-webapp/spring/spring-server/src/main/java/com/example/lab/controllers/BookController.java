package com.example.lab.controllers;

import com.example.lab.entities.book.Book;
import com.example.lab.services.BookStatsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.lab.services.BookService;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/books")
@CrossOrigin(originPatterns = "*")
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class BookController {
    private final BookService bookService;

    @GetMapping("")
    @RolesAllowed({"library-user", "library-admin"})
    public ResponseEntity getAll() {
        var books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    @RolesAllowed("library-admin")
    public ResponseEntity get(@PathVariable("id") long id) {
        var book = bookService.find(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping("")
    @RolesAllowed("library-admin")
    public void create(@RequestParam String name,
                       @RequestParam String author,
                       @RequestParam String lang) {
        var book = new Book(0, name, author, lang);
        bookService.create(book);
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable("id") long id,
                       @RequestParam String name,
                       @RequestParam String author,
                       @RequestParam String lang) {
        var book = new Book(id, name, author, lang);
        bookService.update(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        bookService.delete(id);
    }
}
