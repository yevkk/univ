package entity.book;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookTest {
    private void helper(Book book1, Book book2) {
        assertEquals("Name1", book1.getName());
        assertEquals("Author1", book1.getAuthor());
        assertEquals("lang1", book1.getLang());
        assertEquals(2, book1.getTags().length);
        assertEquals("Tag1", book1.getTags()[0]);
        assertEquals("Tag2", book1.getTags()[1]);

        assertEquals("Name2", book2.getName());
        assertEquals("Author2", book2.getAuthor());
        assertEquals("lang2", book2.getLang());
        assertEquals(3, book2.getTags().length);
        assertEquals("Tag2", book2.getTags()[0]);
        assertEquals("Tag3", book2.getTags()[1]);
        assertEquals("Tag4", book2.getTags()[2]);
    }

    @Test
    public void emptyConstructor() {
        var book = new Book();

        assertEquals(-1, book.getId());
        assertNull(book.getName());
        assertNull(book.getAuthor());
        assertNull(book.getLang());
        assertNull(book.getTags());
    }

    @Test
    public void constructor() {
        var book1 = new Book(4, "Name1", "Author1", "lang1", new String[]{"Tag1", "Tag2"});
        var book2 = new Book(37, "Name2", "Author2", "lang2", new String[]{"Tag2", "Tag3", "Tag4"});

        assertEquals(4, book1.getId());
        assertEquals(37, book2.getId());
        helper(book1, book2);
    }

    @Test
    public void constructorAllArgsLombok() {
        var book1 = new Book("Name1", "Author1", "lang1", new String[]{"Tag1", "Tag2"});
        var book2 = new Book("Name2", "Author2", "lang2", new String[]{"Tag2", "Tag3", "Tag4"});

        assertEquals(-1, book1.getId());
        assertEquals(-1, book2.getId());
        helper(book1, book2);
    }

    @Test
    public void setters() {
        var book1 = new Book();
        book1.setId(4);
        book1.setName("Name1");
        book1.setAuthor("Author1");
        book1.setLang("lang1");
        book1.setTags(new String[]{"Tag1", "Tag2"});

        var book2 = new Book("Name2", "Author2", "lang2", new String[]{"Tag2", "Tag3", "Tag4"});
        book2.setId(37);
        book2.setName("Name2");
        book2.setAuthor("Author2");
        book2.setLang("lang2");
        book2.setTags(new String[]{"Tag2", "Tag3", "Tag4"});

        assertEquals(4, book1.getId());
        assertEquals(37, book2.getId());
        helper(book1, book2);
    }

}