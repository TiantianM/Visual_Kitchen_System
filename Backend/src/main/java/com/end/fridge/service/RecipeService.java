package com.end.fridge.service;

import com.end.fridge.domain.Fridge;
import com.end.fridge.domain.PreferenceHistory;
import com.end.fridge.domain.Recipe;
import com.end.fridge.domain.RecommendResult;
import com.end.fridge.repository.PreferenceHistoryRepository;
import com.end.fridge.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private PreferenceHistoryRepository preferenceRepository;

    public Iterable<Recipe> list() {
        return recipeRepository.findAll();
    }

    public List<Recipe> findLimitedRecipes() {
        return recipeRepository.findTop10ByOrderByIdDesc();
    }

    public List<RecommendResult> findRecipesByFridgesAndNum(List<Fridge> fridges, int num) {
        Long uid = 0L;
        List<String> ingredientNames = new ArrayList<>();
        for (Fridge fridge : fridges) {
            if (uid == 0L) {
                uid = fridge.getUid();
            }
            ingredientNames.add(fridge.getIngredient() == null ? fridge.getOtherName() : fridge.getIngredient().getName());
        }

        List<List<String>> combinations = generateCombinations(ingredientNames);
        List<Recipe> allRecipes = (List<Recipe>) recipeRepository.findAll();
        Set<RecommendResult> recommendResults = new HashSet<>();

        // Loop through all the possible combinations
        for (List<String> combination : combinations) {
            if (combination.isEmpty()) {continue;}
            // Loop through all the recipes
            for (Recipe recipe : allRecipes) {
                if (recipe.getIngredients().isEmpty()) {continue;}
                String[] recipeIngredients = recipe.getIngredients().split("&&");

                // Store unmatched ingredients
                List<String> unmatchedIngredients = getUnmatchedIngredients(combination, recipeIngredients);
                // Calculate the number of unmatched ingredients
                int unMatchCount = unmatchedIngredients.size();
                if ((unMatchCount <= num && Math.abs(recipeIngredients.length - combination.size()) > num) || unMatchCount == 0) {
                    RecommendResult recommendResult = new RecommendResult(recipe, unMatchCount, unmatchedIngredients);
                    recommendResults.add(recommendResult);
                }
            }
        }

        List<RecommendResult> likeList = new ArrayList<>();
        Set<RecommendResult> normalList = new HashSet<>();
        List<RecommendResult> disLikeList = new ArrayList<>();
        Iterable<PreferenceHistory> preferenceHistories = preferenceRepository.findByUid(uid);
        for (RecommendResult recommendResult : recommendResults) {
            Long recipeId = recommendResult.getRecipe().getId();
            boolean inPre = false;
            for (PreferenceHistory preference : preferenceHistories) {
                if (preference.getRid().equals(recipeId)) {
                    inPre = true;
                    if (preference.getLikeCount() > 0) {
                        likeList.add(recommendResult);
                    } else if ((preference.getDislikeCount() > 0)) {
                        disLikeList.add(recommendResult);
                    } else {
                        normalList.add(recommendResult);
                    }
                }
            }
            if (!inPre) {
                normalList.add(recommendResult);
            }
        }
        sortRecommendResults(likeList, disLikeList, preferenceHistories);
        likeList.addAll(normalList);
        likeList.addAll(disLikeList);

        return likeList;
    }

    private static List<String> getUnmatchedIngredients(List<String> combination, String[] recipeIngredients) {
        Set<String> unmatchedIngredients = new HashSet<>();
//        RecommendResult recommendResult = new RecommendResult();
        for (String ingredient : combination) {
            // Loop through each Ingredient
            for (String recipeIngredient : recipeIngredients) {
                if (!recipeIngredient.toLowerCase().contains(ingredient.toLowerCase())) {
                    unmatchedIngredients.add(recipeIngredient);
                }
            }
        }
        return unmatchedIngredients.stream().toList();
    }

    private List<List<String>> generateCombinations(List<String> ingredientNames) {
        List<List<String>> combinations = new ArrayList<>();
        backtrack(combinations, new ArrayList<>(), ingredientNames, 0);
        return combinations;
    }

    private void backtrack(List<List<String>> combinations, List<String> tempList, List<String> ingredientNames, int start) {
        if (tempList.size() > 7) {
            return;
        }
        combinations.add(new ArrayList<>(tempList));
        for (int i = start; i < ingredientNames.size(); i++) {
            if (!tempList.contains(ingredientNames.get(i))) {
                tempList.add(ingredientNames.get(i));
                backtrack(combinations, tempList, ingredientNames, i + 1);
                tempList.remove(tempList.size() - 1);
            }
        }
    }

    public void sortRecommendResults(List<RecommendResult> likeList, List<RecommendResult> disLikeList, Iterable<PreferenceHistory> preferenceHistories) {
        likeList.sort(Comparator.comparingLong(like -> getLikeCount((RecommendResult) like, preferenceHistories)).reversed());
        disLikeList.sort(Comparator.comparingLong(dislike -> getDislikeCount(dislike, preferenceHistories)));
    }

    private long getLikeCount(RecommendResult recommendResult, Iterable<PreferenceHistory> preferenceHistories) {
        return getPreferenceCount(recommendResult, preferenceHistories, true);
    }

    private long getDislikeCount(RecommendResult recommendResult, Iterable<PreferenceHistory> preferenceHistories) {
        return getPreferenceCount(recommendResult, preferenceHistories, false);
    }

    private long getPreferenceCount(RecommendResult recommendResult, Iterable<PreferenceHistory> preferenceHistories, boolean isLike) {
        long recipeId = recommendResult.getRecipe().getId();
        for (PreferenceHistory preferenceHistory : preferenceHistories) {
            if (preferenceHistory.getRid() == recipeId) {
                return isLike ? preferenceHistory.getLikeCount() : preferenceHistory.getDislikeCount();
            }
        }
        return 0;
    }
}
