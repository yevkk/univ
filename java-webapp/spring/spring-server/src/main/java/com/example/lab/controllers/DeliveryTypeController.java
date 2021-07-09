package com.example.lab.controllers;

import com.example.lab.entities.book.request.misc.DeliveryType;
import com.example.lab.services.DeliveryTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/delivery_types")
@CrossOrigin(originPatterns = "*")
@AllArgsConstructor
public class DeliveryTypeController {
    private final DeliveryTypeService deliveryTypeService;

    @GetMapping("")
    public ResponseEntity getAll() {
        var deliveryTypes = deliveryTypeService.findAll();
        return ResponseEntity.ok(deliveryTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") long id) {
        var deliveryType = deliveryTypeService.find(id);
        return ResponseEntity.ok(deliveryType);
    }

    @PostMapping("")
    public void create(@RequestParam String description) {
        var deliveryType = new DeliveryType(0, description);
        deliveryTypeService.create(deliveryType);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        deliveryTypeService.delete(id);
    }
}
