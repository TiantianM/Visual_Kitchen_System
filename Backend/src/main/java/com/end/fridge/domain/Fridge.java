package com.end.fridge.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Entity
@Table(name = "fridge")
public class Fridge {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @NotNull(message = "uid cannot be null")
    private Long uid; // User Id
    private Long iid; // Ingredient Id

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "iid", referencedColumnName = "id", insertable = false, updatable = false)
    private Ingredient ingredient; //

    @NotNull(message = "quantity cannot be null")
    @Positive(message = "quantity must be positive")
    private Integer quantity;
    private String otherName;
    private String otherImg;
    @NotNull(message = "expirationDate cannot be null")
    private LocalDateTime expirationDate;

    @AssertTrue(message = "either iId or other_name must be provided")
    private boolean isEitherIIdOrOtherNameProvided() {
        return (iid != null || otherName != null) && !(iid != null && otherName != null);
    }

    public Fridge() {}
}
