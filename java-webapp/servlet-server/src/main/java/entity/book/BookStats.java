package entity.book;

import entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BookStats extends Entity {
    private Book book;
    private int amount;
    private int totalRequests;
    private double rate;

    public BookStats(int id, Book book, int amount, int totalRequests, double rate) {
        this(book, amount, totalRequests, rate);
        setId(id);
    }
}
