package com.example.lab.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String login;

    @Column
    private String password;

    @Column(columnDefinition = "text")
    private Role role;
}
