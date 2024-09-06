package com.end.fridge.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecommendResult {
    private Recipe recipe;
    private Integer unMatchCount;
    private List<String> unmatchedIngredients;

    public RecommendResult() {}
}
