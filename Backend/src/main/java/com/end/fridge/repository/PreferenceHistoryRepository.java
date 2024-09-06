package com.end.fridge.repository;

import com.end.fridge.domain.PreferenceHistory;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PreferenceHistoryRepository extends CrudRepository<PreferenceHistory, Long> {
    Optional<PreferenceHistory> findByUidAndRid(Long uid, Long rid);

    Iterable<PreferenceHistory> findByUid(Long uid);
}
