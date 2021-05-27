package entity.book;



import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookStatsTest {
    private void helper(BookStats bookStats1, BookStats bookStats2) {
        assertEquals(2, bookStats1.getBookID());
        assertEquals(3, bookStats1.getAmount());
        assertEquals(4, bookStats1.getTotalRequests());
        assertEquals(5, bookStats1.getRate(), 0.001);

        assertEquals(3, bookStats2.getBookID());
        assertEquals(4, bookStats2.getAmount());
        assertEquals(5, bookStats2.getTotalRequests());
        assertEquals(6, bookStats2.getRate(), 0.001);
    }

    @Test
    public void emptyConstructor() {
        var bookStats = new BookStats();

        assertEquals(-1, bookStats.getId());
        assertEquals(0, bookStats.getBookID());
        assertEquals(0, bookStats.getAmount());
        assertEquals(0, bookStats.getTotalRequests());
        assertEquals(0, bookStats.getRate(), 0.001);
    }

    @Test
    public void constructor() {
        var bookStats1 = new BookStats(1, 2, 3, 4, 5.0);
        var bookStats2 = new BookStats(2, 3, 4, 5, 6.0);

        assertEquals(1, bookStats1.getId());
        assertEquals(2, bookStats2.getId());
        helper(bookStats1, bookStats2);
    }

    @Test
    public void constructorAllArgsLombok() {
        var bookStats1 = new BookStats(2, 3, 4, 5);
        var bookStats2 = new BookStats(3, 4, 5, 6);

        assertEquals(-1, bookStats1.getId());
        assertEquals(-1, bookStats2.getId());
        helper(bookStats1, bookStats2);
    }

    @Test
    public void setters() {
        var bookStats1 = new BookStats();
        bookStats1.setId(1);
        bookStats1.setBookID(2);
        bookStats1.setAmount(3);
        bookStats1.setTotalRequests(4);
        bookStats1.setRate(5);

        var bookStats2 = new BookStats();
        bookStats2.setId(2);
        bookStats2.setBookID(3);
        bookStats2.setAmount(4);
        bookStats2.setTotalRequests(5);
        bookStats2.setRate(6);

        assertEquals(1, bookStats1.getId());
        assertEquals(2, bookStats2.getId());
        helper(bookStats1, bookStats2);
    }
}