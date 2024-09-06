package com.end.fridge.controller;

import com.end.fridge.domain.Fridge;
import com.end.fridge.domain.Recipe;
import com.end.fridge.domain.RecommendResult;
import com.end.fridge.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipe")
@CrossOrigin
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        Iterable<Recipe> recipes = recipeService.list();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/list_limit")
    public ResponseEntity<?> listLimit() {
        Iterable<Recipe> recipes = recipeService.findLimitedRecipes();
        return ResponseEntity.ok(recipes);
    }

    @PostMapping("/recommend")
    public ResponseEntity<?> findRecipes(@RequestBody List<@Valid Fridge> fridges) {
        List<RecommendResult> recommendResults = recipeService.findRecipesByFridgesAndNum(fridges, 0);
        return ResponseEntity.ok(recommendResults);
    }
}
