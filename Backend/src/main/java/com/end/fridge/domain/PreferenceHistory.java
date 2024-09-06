package com.end.fridge.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "preference_history")
public class PreferenceHistory {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private Long uid; // User Id
    private Long rid; // Recipe Id

    private Long likeCount;
    private Long dislikeCount;

    public PreferenceHistory() {

    }
}
