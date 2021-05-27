package entity.book.request;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class GetBookRequestTest {
    private void helper(GetBookRequest req1, GetBookRequest req2) {
        assertEquals(LocalDateTime.of(2020, Month.APRIL, 25, 6, 30, 20), req1.getDatetime());
        assertEquals(2, req1.getUserID());
        assertEquals(4, req1.getBookID());
        assertEquals(1, req1.getDeliveryTypeID());
        assertEquals("", req1.getContact());
        assertEquals(RequestState.IN_PROGRESS, req1.getState());

        assertEquals(LocalDateTime.of(2021, Month.MAY, 30, 18, 25, 0), req2.getDatetime());
        assertEquals(3, req2.getUserID());
        assertEquals(5, req2.getBookID());
        assertEquals(2, req2.getDeliveryTypeID());
        assertEquals("@test", req2.getContact());
        assertEquals(RequestState.SENT, req2.getState());
    }

    @Test
    public void emptyConstructor() {
        var req = new GetBookRequest();

        assertEquals(-1, req.getId());
        assertNull(req.getDatetime());
        assertEquals(0, req.getUserID());
        assertEquals(0, req.getBookID());
        assertEquals(0, req.getDeliveryTypeID());
        assertNull(req.getContact());
        assertNull(req.getState());
    }

    @Test
    public void constructor() {
        var req1 = new GetBookRequest(1, LocalDateTime.parse("2020-04-25T06:30:20"), 2, 4, 1, "", RequestState.IN_PROGRESS);
        var req2 = new GetBookRequest(3, LocalDateTime.parse("2021-05-30T18:25:00"), 3, 5, 2, "@test", RequestState.SENT);

        assertEquals(1, req1.getId());
        assertEquals(3, req2.getId());
        helper(req1, req2);
    }

    @Test
    public void constructorAllArgsLombok() {
        var req1 = new GetBookRequest(LocalDateTime.parse("2020-04-25T06:30:20"), 2, 4, 1, "", RequestState.IN_PROGRESS);
        var req2 = new GetBookRequest(LocalDateTime.parse("2021-05-30T18:25:00"), 3, 5, 2, "@test", RequestState.SENT);

        assertEquals(-1, req1.getId());
        assertEquals(-1, req2.getId());
        helper(req1, req2);
    }

    @Test
    public void setters() {
        var req1 = new GetBookRequest();
        req1.setId(1);
        req1.setDatetime(LocalDateTime.parse("2020-04-25T06:30:20"));
        req1.setUserID(2);
        req1.setBookID(4);
        req1.setDeliveryTypeID(1);
        req1.setContact("");
        req1.setState(RequestState.IN_PROGRESS);

        var req2 = new GetBookRequest();
        req2.setId(3);
        req2.setDatetime(LocalDateTime.parse("2021-05-30T18:25:00"));
        req2.setUserID(3);
        req2.setBookID(5);
        req2.setDeliveryTypeID(2);
        req2.setContact("@test");
        req2.setState(RequestState.SENT);

        assertEquals(1, req1.getId());
        assertEquals(3, req2.getId());
        helper(req1, req2);
    }

}