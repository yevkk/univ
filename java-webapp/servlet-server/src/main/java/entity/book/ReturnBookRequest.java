package entity.book;

import entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReturnBookRequest extends Entity {
    private int getBookRequestID;
    private RequestState state;

    public ReturnBookRequest(int id, int getBookRequestID, RequestState state) {
        this(getBookRequestID, state);
        setId(id);
    }
}
