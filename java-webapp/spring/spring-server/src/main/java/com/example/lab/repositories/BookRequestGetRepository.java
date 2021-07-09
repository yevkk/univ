package com.example.lab.repositories;

import com.example.lab.entities.book.request.BookRequestGet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRequestGetRepository extends JpaRepository<BookRequestGet, Long> {
    List<BookRequestGet> findAllByUserID(String userID);
}
