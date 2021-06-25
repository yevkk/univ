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
public class BookRateLogRecord extends Entity {
    private LocalDateTime datetime;
    private int bookID;
    private int userID;
    private double contribution;

    public BookRateLogRecord(int id, LocalDateTime datetime, int bookID, int userID, double contribution) {
        this(datetime, bookID, userID, contribution);
        setId(id);
    }
}
