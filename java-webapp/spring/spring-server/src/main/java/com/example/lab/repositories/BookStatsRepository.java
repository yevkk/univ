package com.example.lab.repositories;

import com.example.lab.entities.book.BookStats;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookStatsRepository extends CrudRepository<BookStats, Long> {
    public BookStats findByBookId(long id);
}
