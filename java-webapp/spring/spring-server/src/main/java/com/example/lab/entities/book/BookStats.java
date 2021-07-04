package com.example.lab.entities.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "book_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int amount;

    @Column(name = "total_requests")
    private int totalRequests;

    @Column
    private double rate;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    private Book book;

    public BookStats(Book book) {
        this.book = book;
    }
}
