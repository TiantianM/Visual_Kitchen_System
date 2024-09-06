package com.end.fridge.repository;

import com.end.fridge.domain.Ingredient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
    List<Ingredient> findByNameContaining(String keyword);
    Optional<Ingredient> findByName(String name);

    @Query("SELECT i FROM Ingredient i WHERE i.name LIKE :keyword%")
    List<Ingredient> findByNameContainingRight(@Param("keyword") String keyword);
    @Query("SELECT i FROM Ingredient i WHERE " +
            "(i.name LIKE :keyword%)" +
            " OR " +
            "(i.name LIKE '% ' || :keyword || '%' AND NOT EXISTS (SELECT 1 FROM Ingredient WHERE name LIKE :keyword%))")
    List<Ingredient> findByNameContainingRightWithLogic(@Param("keyword") String keyword);
}
