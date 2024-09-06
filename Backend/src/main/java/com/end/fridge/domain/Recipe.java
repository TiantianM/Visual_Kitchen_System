package com.end.fridge.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;
    private String title;
    private String readyIn; //Expected cooking time
    private String img;
    @Column(columnDefinition = "LONGTEXT")
    private String directions; // A string representation of the cooking instruction
    @Column(columnDefinition = "LONGTEXT")
    private String ingredients;

    public Recipe() {}
}
