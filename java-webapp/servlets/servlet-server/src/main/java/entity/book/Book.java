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
public class Book extends Entity {
    private String name;
    private String author;
    private String lang;
    private String[] tags;

    public Book(int id, String name, String author, String lang, String[] tags) {
        this(name, author, lang, tags);
        setId(id);
    }
}
