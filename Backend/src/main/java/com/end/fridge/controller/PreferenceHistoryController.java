package com.end.fridge.controller;

import com.end.fridge.domain.PreferenceHistory;
import com.end.fridge.service.PreferenceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/preference_history")
@CrossOrigin
public class PreferenceHistoryController {
    @Autowired
    private PreferenceHistoryService preferenceService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody PreferenceHistory preferenceHistory) {
        PreferenceHistory history = preferenceService.save(preferenceHistory);
        return ResponseEntity.ok(history);
    }
}
