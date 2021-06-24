package entity.misc;

import entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class User extends Entity {
    public enum Role {
        USER, ADMIN, UNDEFINED;

        @Override
        public String toString() {
            return switch (this) {
                case USER -> "user";
                case ADMIN -> "admin";
                case UNDEFINED -> "undefined";
            };
        }
    }

    private String login;
    private String password;
    private Role role;
    private String contact;

    public User(int id, String login, String password, Role role, String contact) {
        this(login, password, role, contact);
        setId(id);
    }
}
