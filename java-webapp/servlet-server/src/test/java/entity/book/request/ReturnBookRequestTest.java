package entity.book.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReturnBookRequestTest {
    private void helper(ReturnBookRequest rReq1, ReturnBookRequest rReq2) {
        assertEquals(4, rReq1.getGetBookRequestID());
        assertEquals(RequestState.PROCESSED, rReq1.getState());

        assertEquals(7, rReq2.getGetBookRequestID());
        assertEquals(RequestState.REJECTED, rReq2.getState());
    }

    @Test
    public void emptyConstructor() {
        var rReq = new ReturnBookRequest();

        assertEquals(-1, rReq.getId());
        assertEquals(0, rReq.getGetBookRequestID());
        assertNull(rReq.getState());
    }

    @Test
    public void constructor() {
        var rReq1 = new ReturnBookRequest(4, 4, RequestState.PROCESSED);
        var rReq2 = new ReturnBookRequest(37, 7, RequestState.REJECTED);

        assertEquals(4, rReq1.getId());
        assertEquals(37, rReq2.getId());
        helper(rReq1, rReq2);
    }

    @Test
    public void constructorAllArgsLombok() {
        var rReq1 = new ReturnBookRequest(4, RequestState.PROCESSED);
        var rReq2 = new ReturnBookRequest(7, RequestState.REJECTED);

        assertEquals(-1, rReq1.getId());
        assertEquals(-1, rReq2.getId());
        helper(rReq1, rReq2);
    }

    @Test
    public void setters() {
        var rReq1 = new ReturnBookRequest();
        rReq1.setId(4);
        rReq1.setGetBookRequestID(4);
        rReq1.setState(RequestState.PROCESSED);

        var rReq2 = new ReturnBookRequest();
        rReq2.setId(37);
        rReq2.setGetBookRequestID(7);
        rReq2.setState(RequestState.REJECTED);

        assertEquals(4, rReq1.getId());
        assertEquals(37, rReq2.getId());
        helper(rReq1, rReq2);
    }

}