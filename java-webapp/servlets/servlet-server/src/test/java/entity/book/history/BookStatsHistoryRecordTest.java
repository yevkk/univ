package entity.book.history;

import entity.book.BookStats;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookStatsHistoryRecordTest {
    private final BookStats stats1 = new BookStats(1, 2, 3, 4, 5);
    private final BookStats stats2 = new BookStats(7, 8, 9, 10, 3);

    private void helper(BookStatsHistoryRecord record1, BookStatsHistoryRecord record2) {
        assertEquals(LocalDate.parse("2020-12-04"), record1.getDate());
        assertEquals(stats1, record1.getStats());

        assertEquals(LocalDate.parse("2021-03-04"), record2.getDate());
        assertEquals(stats2, record2.getStats());
    }

    @Test
    public void emptyConstructor() {
        var record = new BookStatsHistoryRecord();

        assertEquals(-1, record.getId());
        assertNull(record.getDate());
        assertNull(record.getStats());
    }

    @Test
    public void constructor() {
        var record1 = new BookStatsHistoryRecord(1, LocalDate.of(2020, 12, 4), stats1);
        var record2 = new BookStatsHistoryRecord(4, LocalDate.of(2021, 3, 4), stats2);

        assertEquals(1, record1.getId());
        assertEquals(4, record2.getId());
        helper(record1, record2);
    }

    @Test
    public void constructorAllArgsLombok() {
        var record1 = new BookStatsHistoryRecord(LocalDate.of(2020, 12, 4), stats1);
        var record2 = new BookStatsHistoryRecord(LocalDate.of(2021, 3, 4), stats2);

        assertEquals(-1, record1.getId());
        assertEquals(-1, record2.getId());
        helper(record1, record2);
    }

    @Test
    public void setters() {
        var record1 = new BookStatsHistoryRecord();
        record1.setId(1);
        record1.setDate(LocalDate.of(2020, 12, 4));
        record1.setStats(stats1);

        var record2 = new BookStatsHistoryRecord(4, LocalDate.of(2021, 3, 4), stats2);
        record2.setId(4);
        record2.setDate(LocalDate.of(2021, 3, 4));
        record2.setStats(stats2);

        assertEquals(1, record1.getId());
        assertEquals(4, record2.getId());
        helper(record1, record2);
    }

}