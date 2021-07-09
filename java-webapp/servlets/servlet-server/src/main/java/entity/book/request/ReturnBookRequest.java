package entity.book.request;

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
public class ReturnBookRequest extends Entity {
    private LocalDateTime datetime;
    private int getBookRequestID;
    private RequestState state;

    public ReturnBookRequest(int id, LocalDateTime datetime, int getBookRequestID, RequestState state) {
        this(datetime, getBookRequestID, state);
        setId(id);
    }
}
