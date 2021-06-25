package entity.book.request;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class ReturnBookRequestTest {
    private void helper(ReturnBookRequest rReq1, ReturnBookRequest rReq2) {
        assertEquals(LocalDateTime.of(2020, Month.APRIL, 25, 6, 30, 20), rReq1.getDatetime());
        assertEquals(4, rReq1.getGetBookRequestID());
        assertEquals(RequestState.PROCESSED, rReq1.getState());

        assertEquals(LocalDateTime.of(2021, Month.MAY, 30, 18, 25, 0), rReq2.getDatetime());
        assertEquals(7, rReq2.getGetBookRequestID());
        assertEquals(RequestState.REJECTED, rReq2.getState());
    }

    @Test
    public void emptyConstructor() {
        var rReq = new ReturnBookRequest();

        assertEquals(-1, rReq.getId());
        assertEquals(0, rReq.getGetBookRequestID());
        assertNull(rReq.getState());
        assertNull(rReq.getDatetime());
    }

    @Test
    public void constructor() {
        var rReq1 = new ReturnBookRequest(4, LocalDateTime.parse("2020-04-25T06:30:20"), 4, RequestState.PROCESSED);
        var rReq2 = new ReturnBookRequest(37, LocalDateTime.parse("2021-05-30T18:25:00"), 7, RequestState.REJECTED);

        assertEquals(4, rReq1.getId());
        assertEquals(37, rReq2.getId());
        helper(rReq1, rReq2);
    }

    @Test
    public void constructorAllArgsLombok() {
        var rReq1 = new ReturnBookRequest(LocalDateTime.parse("2020-04-25T06:30:20"), 4, RequestState.PROCESSED);
        var rReq2 = new ReturnBookRequest(LocalDateTime.parse("2021-05-30T18:25:00"), 7, RequestState.REJECTED);

        assertEquals(-1, rReq1.getId());
        assertEquals(-1, rReq2.getId());
        helper(rReq1, rReq2);
    }

    @Test
    public void setters() {
        var rReq1 = new ReturnBookRequest();
        rReq1.setId(4);
        rReq1.setDatetime(LocalDateTime.parse("2020-04-25T06:30:20"));
        rReq1.setGetBookRequestID(4);
        rReq1.setState(RequestState.PROCESSED);

        var rReq2 = new ReturnBookRequest();
        rReq2.setId(37);
        rReq2.setDatetime(LocalDateTime.parse("2021-05-30T18:25:00"));
        rReq2.setGetBookRequestID(7);
        rReq2.setState(RequestState.REJECTED);

        assertEquals(4, rReq1.getId());
        assertEquals(37, rReq2.getId());
        helper(rReq1, rReq2);
    }

}