package com.example.lab.entities.book.changelogs;

import com.example.lab.entities.book.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_balance_changelog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceLogRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDateTime datetime;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column
    private int amount;

    @Column
    private String comment;
}
