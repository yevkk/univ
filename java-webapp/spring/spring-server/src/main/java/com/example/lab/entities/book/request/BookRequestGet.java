package com.example.lab.entities.book.request;

import com.example.lab.entities.book.Book;
import com.example.lab.entities.book.request.misc.DeliveryType;
import com.example.lab.entities.book.request.misc.RequestState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestGet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private LocalDateTime datetime;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "user_id")
    private String userID;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_type_id")
    private DeliveryType deliveryType;

    @Column
    private String contact;

    @Column(columnDefinition = "text")
    private RequestState state;
}
