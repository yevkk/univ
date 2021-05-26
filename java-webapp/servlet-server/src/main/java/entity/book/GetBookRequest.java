package entity.book;

import entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GetBookRequest extends Entity {
    private Date datetime;
    private int userID;
    private int bookID;
    private int deliveryTypeID;
    private String contact;
    private RequestState state;

    public GetBookRequest(int id, Date datetime, int userID, int bookID, int deliveryTypeID, String contact, RequestState state) {
        this(datetime, userID, bookID, deliveryTypeID, contact, state);
        setId(id);
    }
}
