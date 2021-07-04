package com.example.lab.repositories;

import com.example.lab.entities.book.request.BookRequestReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRequestReturnRepository extends JpaRepository<BookRequestReturn, Long> {
    public List<BookRequestReturn> findByRequestUserID(long id);
}
