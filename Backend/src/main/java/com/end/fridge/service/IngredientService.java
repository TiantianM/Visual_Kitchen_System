package com.end.fridge.service;

import com.end.fridge.domain.Ingredient;
import com.end.fridge.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;

    public Iterable<Ingredient> list() {
        return ingredientRepository.findAll();
    }

    public List<Ingredient> findByNameContaining(String keyword) {
        return ingredientRepository.findByNameContainingRightWithLogic(keyword);
    }

    public Optional<Ingredient> findByName(String name) {
        return ingredientRepository.findByName(name);
    }

    public List<Ingredient> searchIngredients(List<String> names) {
        List<Ingredient> resultList = new ArrayList<>();

        for (String name : names) {
            List<Ingredient> ingredients = ingredientRepository.findByNameContainingRightWithLogic(name);
            if (!ingredients.isEmpty()) {
                resultList.add(ingredients.get(0));
            }
        }

        return resultList;
    }
}
