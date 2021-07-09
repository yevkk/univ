package servlet.books;

import connect.ConnectionPool;
import connect.dao.BookDAO;
import entity.book.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BooksServletTest extends Mockito {
    private static ConnectionPool pool = new ConnectionPool("database-test");

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter writer;

    private final Book[] books = {
            new Book(1, "book1", "author1", "lang1", new String[]{"tag1", "tag2"}),
            new Book(2, "book2", "author2", "lang2", new String[]{"tag1"}),
            new Book(3, "book3", "author3", "lang3", new String[]{"tag2", "tag3"}),
    };

//    @BeforeEach
    public void mockSetup() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("login")).thenReturn("superuser");
        when(request.getParameter("password")).thenReturn("pass");

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
    }

//    @Test
    public void findAllTest() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn(null);
        
        var dao = mock(BookDAO.class);
        when(dao.findAll()).thenReturn(Arrays.asList(books));

        new BooksServlet().doGet(request, response);

        writer.flush();
//        System.out.println(stringWriter.toString());

        assertTrue(stringWriter.toString().contains("{\"name\":\"book1\",\"author\":\"author1\",\"lang\":\"lang1\",\"tags\":[\"tag1\",\"tag2\"],\"id\":1}"));
        assertTrue(stringWriter.toString().contains("{\"name\":\"book2\",\"author\":\"author2\",\"lang\":\"lang2\",\"tags\":[\"tag1\"],\"id\":2}"));
        assertTrue(stringWriter.toString().contains("{\"name\":\"book3\",\"author\":\"author3\",\"lang\":\"lang3\",\"tags\":[\"tag2\",\"tag3\"],\"id\":3}"));
    }

//    @Test
    public void findByIDTest() throws ServletException, IOException {

    }

}