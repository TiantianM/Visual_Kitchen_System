package com.end.fridge.repository;

import com.end.fridge.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    List<Recipe> findTop10ByOrderByIdDesc();
}
