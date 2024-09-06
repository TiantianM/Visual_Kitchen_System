package com.end.fridge.controller;

import com.end.fridge.domain.Fridge;
import com.end.fridge.service.FridgeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fridge")
@CrossOrigin
public class FridgeController {
    @Autowired
    private FridgeService fridgeService;

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        Iterable<Fridge> fridges = fridgeService.list();
        return ResponseEntity.ok(fridges);
    }

    @GetMapping("/list/{uid}")
    public ResponseEntity<?> listByUid(@PathVariable Long uid) {
        Iterable<Fridge> fridges = fridgeService.listByUid(uid);
        return ResponseEntity.ok(fridges);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody @Valid Fridge fridge) {
        Fridge savedFridge = fridgeService.save(fridge);
        return ResponseEntity.ok(savedFridge);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody @Valid Fridge fridge) {
        Fridge savedFridge = fridgeService.update(fridge);
        return ResponseEntity.ok(savedFridge);
    }

    @PostMapping("/saveAll")
    public ResponseEntity<?> saveAll(@RequestBody List<@Valid Fridge> fridges) {
        Iterable<Fridge> savedFridges = fridgeService.saveAll(fridges);
        return ResponseEntity.ok(savedFridges);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        fridgeService.deleteById(id);
        return ResponseEntity.ok("Fridge with ID: " + id + " has been deleted.");
    }
}
