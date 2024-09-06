package com.end.fridge.controller;

import com.end.fridge.domain.Ingredient;
import com.end.fridge.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ingredient")
@CrossOrigin
public class IngredientController {
    @Autowired
    private IngredientService ingredientService;

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        Iterable<Ingredient> ingredients = ingredientService.list();
        return ResponseEntity.ok(ingredients);
    }
    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> search(@PathVariable String keyword) {
        // Check if the user input keyword has length greater than 2
        if (keyword.length() < 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Too few characters entered");
        }

        Iterable<Ingredient> result = ingredientService.findByNameContaining(keyword);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> findIngredientByName(@PathVariable String name) {
        Optional<Ingredient> ingredientOptional = ingredientService.findByName(name);
        if (ingredientOptional.isPresent()) {
            return ResponseEntity.ok(ingredientOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingredient not found");
        }
    }

    @PostMapping("/search_list")
    public ResponseEntity<List<Ingredient>> earchList(@RequestBody List<String> names) {
        List<Ingredient> resultList = ingredientService.searchIngredients(names);
        return ResponseEntity.ok(resultList);
    }
}
