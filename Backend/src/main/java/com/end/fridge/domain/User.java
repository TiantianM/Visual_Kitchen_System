package com.end.fridge.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    private String name;
    private String email;
    private String password;
    private String token;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
