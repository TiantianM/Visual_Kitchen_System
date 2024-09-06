package com.end.fridge.service;

import com.end.fridge.domain.Fridge;
import com.end.fridge.repository.FridgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FridgeService {
    @Autowired
    private FridgeRepository fridgeRepository;

    public Iterable<Fridge> list() {
        return fridgeRepository.findAll();
    }

    public Iterable<Fridge> listByUid(Long uid) {
        return fridgeRepository.findByUid(uid);
    }

    public Fridge save(Fridge fridge) {
        // Check if there's already ingredients with same ingredients
        Optional<Fridge> existingFridge = fridgeRepository.findByUidAndIidOrOtherNameAndExpirationDate(
                fridge.getUid(), fridge.getIid(), fridge.getOtherName(), fridge.getExpirationDate());

        if (existingFridge.isPresent()) {
            // Update the quantity if there exists
            Fridge existingRecord = existingFridge.get();
            existingRecord.setQuantity(existingRecord.getQuantity() + fridge.getQuantity());
            return fridgeRepository.save(existingRecord);
        } else {
            // If not, create a new one
            return fridgeRepository.save(fridge);
        }
    }

    public Iterable<Fridge> saveAll(List<Fridge> fridges) {
        return fridgeRepository.saveAll(fridges);
    }

    public void deleteById(Long id) {
        fridgeRepository.deleteById(id);
    }

    public Fridge update(Fridge fridge) {
        return fridgeRepository.save(fridge);
    }
}
