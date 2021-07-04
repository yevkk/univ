package com.example.lab.repositories;

import com.example.lab.entities.book.BookStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookStatsRepository extends JpaRepository<BookStats, Long> {
    public BookStats findByBookId(long id);
}
