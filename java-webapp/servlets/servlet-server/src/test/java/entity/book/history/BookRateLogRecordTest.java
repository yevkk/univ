package entity.book.history;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookRateLogRecordTest {
    private void helper(BookRateLogRecord log1, BookRateLogRecord log2) {
        assertEquals(LocalDateTime.parse("2020-07-08T23:23:23"), log1.getDatetime());
        assertEquals(2, log1.getBookID());
        assertEquals(7, log1.getUserID());
        assertEquals(3.5, log1.getContribution(), 0.001);

        assertEquals(LocalDateTime.parse("2021-01-01T02:03:04"), log2.getDatetime());
        assertEquals(5, log2.getBookID());
        assertEquals(8, log2.getUserID());
        assertEquals(5, log2.getContribution(), 0.001);
    }

    @Test
    public void emptyConstructor() {
        var log = new BookRateLogRecord();

        assertEquals(-1, log.getId());
        assertNull(log.getDatetime());
        assertEquals(0, log.getBookID());
        assertEquals(0, log.getUserID());
        assertEquals(0, log.getContribution());
    }

    @Test
    public void constructor() {
        var log1 = new BookRateLogRecord(3, LocalDateTime.of(2020, 7, 8, 23, 23, 23), 2, 7, 3.5);
        var log2 = new BookRateLogRecord(6, LocalDateTime.of(2021, 1, 1, 2, 3, 4), 5, 8, 5);

        assertEquals(3, log1.getId());
        assertEquals(6, log2.getId());
        helper(log1, log2);
    }

    @Test
    public void constructorAllArgsLombok() {
        var log1 = new BookRateLogRecord(LocalDateTime.of(2020, 7, 8, 23, 23, 23), 2, 7, 3.5);
        var log2 = new BookRateLogRecord(LocalDateTime.of(2021, 1, 1, 2, 3, 4), 5, 8, 5);

        assertEquals(-1, log1.getId());
        assertEquals(-1, log2.getId());
        helper(log1, log2);
    }

    @Test
    public void setters() {
        var log1 = new BookRateLogRecord();
        log1.setId(3);
        log1.setDatetime(LocalDateTime.of(2020, 7, 8, 23, 23, 23));
        log1.setBookID(2);
        log1.setUserID(7);
        log1.setContribution(3.5);

        var log2 = new BookRateLogRecord();
        log2.setId(6);
        log2.setDatetime(LocalDateTime.of(2021, 1, 1, 2, 3, 4));
        log2.setBookID(5);
        log2.setUserID(8);
        log2.setContribution(5);

        assertEquals(3, log1.getId());
        assertEquals(6, log2.getId());
        helper(log1, log2);
    }

}