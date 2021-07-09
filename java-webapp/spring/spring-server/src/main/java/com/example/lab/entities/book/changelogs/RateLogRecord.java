package com.example.lab.entities.book.changelogs;

import com.example.lab.entities.book.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_rate_changelog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateLogRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDateTime datetime;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "user_id")
    private int userID;

    @Column
    private double contribution;
}
