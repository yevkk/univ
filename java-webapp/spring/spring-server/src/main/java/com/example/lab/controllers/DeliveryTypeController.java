package com.example.lab.controllers;

import com.example.lab.entities.book.request.misc.DeliveryType;
import com.example.lab.services.DeliveryTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/delivery_types")
@CrossOrigin(originPatterns = "*")
@AllArgsConstructor
public class DeliveryTypeController {
    private final DeliveryTypeService deliveryTypeService;

    @GetMapping("")
    @RolesAllowed({"library-user", "library-admin"})
    public ResponseEntity getAll() {
        var deliveryTypes = deliveryTypeService.findAll();
        return ResponseEntity.ok(deliveryTypes);
    }

    @GetMapping("/{id}")
    @RolesAllowed({"library-user", "library-admin"})
    public ResponseEntity get(@PathVariable("id") long id) {
        var deliveryType = deliveryTypeService.find(id);
        return ResponseEntity.ok(deliveryType);
    }

    @PostMapping("")
    @RolesAllowed({"library-admin"})
    public void create(@RequestParam String description) {
        var deliveryType = new DeliveryType(0, description);
        deliveryTypeService.create(deliveryType);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({"library-admin"})
    public void delete(@PathVariable("id") long id) {
        deliveryTypeService.delete(id);
    }
}
