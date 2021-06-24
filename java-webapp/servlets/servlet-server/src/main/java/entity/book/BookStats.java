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
    private int bookID;
    private int amount;
    private int totalRequests;
    private double rate;

    public BookStats(int id, int bookID, int amount, int totalRequests, double rate) {
        this(bookID, amount, totalRequests, rate);
        setId(id);
    }
}
