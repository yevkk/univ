package com.example.lab.entities.book.request;

import com.example.lab.entities.book.request.misc.RequestState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_request_return")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDateTime datetime;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    private BookRequestGet request;

    @Column(columnDefinition = "text")
    private RequestState state;
}
