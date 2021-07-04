package com.example.lab.repositories;

import com.example.lab.entities.book.request.misc.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryTypeRepository extends JpaRepository<DeliveryType, Long> {
}
