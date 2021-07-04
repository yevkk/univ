package com.example.lab.controllers;

import com.example.lab.services.BookStatsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stats")
@CrossOrigin(originPatterns = "*")
@AllArgsConstructor
public class BookStatsController {
    private final BookStatsService statsService;

    @GetMapping("")
    public ResponseEntity getAll(@RequestParam(value = "book_id", required = false) Long bookId) {
        if (bookId == null) {
            var stats = statsService.findAll();
            return ResponseEntity.ok(stats);
        } else {
            var stats = statsService.findByBookId(bookId);
            return ResponseEntity.ok(stats);
        }
    }

    @PatchMapping("")
    public void getByBookId(@RequestParam("book_id") long bookId,
                            @RequestParam int amount) {
        statsService.update(bookId, amount, "manual update");
    }
}
