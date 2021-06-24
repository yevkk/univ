package entity.book.history;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookBalanceLogRecordTest {
    private void helper(BookBalanceLogRecord log1, BookBalanceLogRecord log2) {
        assertEquals(LocalDateTime.parse("2020-09-08T06:30:20"), log1.getDatetime());
        assertEquals(3, log1.getBookID());
        assertEquals(2, log1.getAmount());
        assertEquals("comment", log1.getComment());

        assertEquals(LocalDateTime.parse("2021-03-03T18:03:40"), log2.getDatetime());
        assertEquals(2, log2.getBookID());
        assertEquals(-1, log2.getAmount());
        assertEquals("", log2.getComment());
    }

    @Test
    public void emptyConstructor() {
        var log = new BookBalanceLogRecord();

        assertEquals(-1, log.getId());
        assertNull(log.getDatetime());
        assertEquals(0, log.getBookID());
        assertEquals(0, log.getAmount());
        assertNull(log.getComment());
    }

    @Test
    public void constructor() {
        var log1 = new BookBalanceLogRecord(3, LocalDateTime.of(2020, 9, 8, 6, 30, 20), 3, 2, "comment");
        var log2 = new BookBalanceLogRecord(7, LocalDateTime.of(2021, 3, 3, 18, 3, 40), 2, -1, "");

        assertEquals(3, log1.getId());
        assertEquals(7, log2.getId());
        helper(log1, log2);
    }

    @Test
    public void constructorAllArgsLombok() {
        var log1 = new BookBalanceLogRecord(LocalDateTime.of(2020, 9, 8, 6, 30, 20), 3, 2, "comment");
        var log2 = new BookBalanceLogRecord(LocalDateTime.of(2021, 3, 3, 18, 3, 40), 2, -1, "");

        assertEquals(-1, log1.getId());
        assertEquals(-1, log2.getId());
        helper(log1, log2);
    }

    @Test
    public void setters() {
        var log1 = new BookBalanceLogRecord();
        log1.setId(3);
        log1.setDatetime(LocalDateTime.of(2020, 9, 8, 6, 30, 20));
        log1.setBookID(3);
        log1.setAmount(2);
        log1.setComment("comment");

        var log2 = new BookBalanceLogRecord();
        log2.setId(7);
        log2.setDatetime(LocalDateTime.of(2021, 3, 3, 18, 3, 40));
        log2.setBookID(2);
        log2.setAmount(-1);
        log2.setComment("");

        assertEquals(3, log1.getId());
        assertEquals(7, log2.getId());
        helper(log1, log2);
    }

}