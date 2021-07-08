package com.example.lab.controllers;

import com.example.lab.entities.book.request.BookRequestGet;
import com.example.lab.entities.book.request.misc.RequestState;
import com.example.lab.services.BookRequestGetService;
import com.example.lab.services.BookService;
import com.example.lab.services.DeliveryTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/requests")
@CrossOrigin(originPatterns = "*")
@AllArgsConstructor
public class BookRequestGetController {
    private final BookRequestGetService requestService;
    private final BookService bookService;
    private final DeliveryTypeService deliveryTypeService;

    @GetMapping("")
    @RolesAllowed({"library-user", "library-admin"})
    public ResponseEntity get(@RequestParam(value = "user_id", required = false) Long userId) {
        List<BookRequestGet> requests;

        if (userId == null) {
            requests = requestService.findAll();
        } else {
            requests = requestService.findByUserId(userId);
        }

        return ResponseEntity.ok(requests);
    }

    @PostMapping("")
    @RolesAllowed({"library-user", "library-admin"})
    public void create(@RequestParam("book_id") long bookId,
                       @RequestParam("user_id") long userId,
                       @RequestParam("delivery_type_id") long deliveryTypeId,
                       @RequestParam String contact) {
        var book = bookService.find(bookId);
        var deliveryType = deliveryTypeService.find(deliveryTypeId);
        var request = new BookRequestGet(0, LocalDateTime.now(), book, userId, deliveryType, contact, RequestState.SENT);
        requestService.create(request);
    }

    @PatchMapping("/{id}")
    @RolesAllowed("library-admin")
    public void update(@PathVariable("id") long id,
                       @RequestParam String state) {
        requestService.update(id, RequestState.valueOf(state.toUpperCase()));
    }
}
