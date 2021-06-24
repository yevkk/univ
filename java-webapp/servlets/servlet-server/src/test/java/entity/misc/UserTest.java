package entity.misc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private void helper(User user1, User user2) {
        assertEquals("login1", user1.getLogin());
        assertEquals("pass1", user1.getPassword());
        assertEquals(User.Role.USER, user1.getRole());
        assertEquals("", user1.getContact());

        assertEquals("login2", user2.getLogin());
        assertEquals("pass2", user2.getPassword());
        assertEquals(User.Role.ADMIN, user2.getRole());
        assertEquals("test@example.com", user2.getContact());
    }

    @Test
    public void emptyConstructor() {
        var user = new User();

        assertEquals(-1, user.getId());
        assertNull(user.getLogin());
        assertNull(user.getPassword());
        assertNull(user.getRole());
        assertNull(user.getContact());
    }

    @Test
    public void constructor() {
        var user1 = new User(3, "login1", "pass1", User.Role.USER, "");
        var user2 = new User(5, "login2", "pass2", User.Role.ADMIN, "test@example.com");

        assertEquals(3, user1.getId());
        assertEquals(5, user2.getId());
        helper(user1, user2);
    }

    @Test
    public void constructorAllArgsLombok() {
        var user1 = new User("login1", "pass1", User.Role.USER, "");
        var user2 = new User("login2", "pass2", User.Role.ADMIN, "test@example.com");

        assertEquals(-1, user1.getId());
        assertEquals(-1, user2.getId());
        helper(user1, user2);
    }

    @Test
    public void setters() {
        var user1 = new User();
        user1.setId(3);
        user1.setLogin("login1");
        user1.setPassword("pass1");
        user1.setRole(User.Role.USER);
        user1.setContact("");

        var user2 = new User();
        user2.setId(5);
        user2.setLogin("login2");
        user2.setPassword("pass2");
        user2.setRole(User.Role.ADMIN);
        user2.setContact("test@example.com");

        assertEquals(3, user1.getId());
        assertEquals(5, user2.getId());
        helper(user1, user2);
    }
}