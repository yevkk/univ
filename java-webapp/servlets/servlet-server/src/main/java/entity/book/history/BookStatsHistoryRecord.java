package entity.book.history;

import entity.Entity;
import entity.book.BookStats;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BookStatsHistoryRecord extends Entity {
    private LocalDate date;
    private BookStats stats;

    public BookStatsHistoryRecord(int id, LocalDate date, BookStats stats) {
        this(date, stats);
        setId(id);
    }
}
