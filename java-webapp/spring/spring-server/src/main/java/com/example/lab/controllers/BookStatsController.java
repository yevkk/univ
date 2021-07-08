package com.example.lab.controllers;

import com.example.lab.services.BookStatsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/stats")
@CrossOrigin(originPatterns = "*")
@AllArgsConstructor
public class BookStatsController {
    private final BookStatsService statsService;

    @GetMapping("")
    @RolesAllowed({"library-user", "library-admin"})
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
    @RolesAllowed({"library-admin"})
    public void updateAmount(@RequestParam("book_id") long bookId,
                             @RequestParam int amount) {
        statsService.update(bookId, amount, "manual update");
    }
}
