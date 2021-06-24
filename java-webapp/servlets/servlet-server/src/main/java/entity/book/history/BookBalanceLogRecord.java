package entity.book.history;

import entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BookBalanceLogRecord extends Entity {
    private LocalDateTime datetime;
    private int bookID;
    private int amount;
    private String comment;

    public BookBalanceLogRecord(int id, LocalDateTime datetime, int bookID, int amount, String comment) {
        this(datetime, bookID, amount, comment);
        setId(id);
    }
}
