package com.end.fridge.service;

import com.end.fridge.domain.PreferenceHistory;
import com.end.fridge.repository.PreferenceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PreferenceHistoryService {
    @Autowired
    private PreferenceHistoryRepository preferenceRepository;

    public PreferenceHistory save(PreferenceHistory preferenceHistory) {
        Optional<PreferenceHistory> historyOptional = preferenceRepository.findByUidAndRid(preferenceHistory.getUid(), preferenceHistory.getRid());

        if (historyOptional.isPresent()) {
            PreferenceHistory existingPreference = historyOptional.get();
            if (existingPreference.getLikeCount() > 0) {
                existingPreference.setLikeCount(existingPreference.getLikeCount() + preferenceHistory.getLikeCount());
                existingPreference.setLikeCount(existingPreference.getLikeCount() - preferenceHistory.getDislikeCount());
            } else if (existingPreference.getDislikeCount() > 0) {
                existingPreference.setDislikeCount(existingPreference.getDislikeCount() - preferenceHistory.getLikeCount());
                existingPreference.setDislikeCount(existingPreference.getDislikeCount() + preferenceHistory.getDislikeCount());
            } else {
                existingPreference.setLikeCount(existingPreference.getLikeCount() + preferenceHistory.getLikeCount());
                existingPreference.setDislikeCount(existingPreference.getDislikeCount() + preferenceHistory.getDislikeCount());
            }
            return preferenceRepository.save(existingPreference);
        } else {
            return preferenceRepository.save(preferenceHistory);
        }
    }
}
