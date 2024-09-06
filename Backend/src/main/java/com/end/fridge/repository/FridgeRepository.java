package com.end.fridge.repository;

import com.end.fridge.domain.Fridge;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FridgeRepository extends CrudRepository<Fridge, Long> {
    @Query("SELECT f FROM Fridge f WHERE f.uid = :uid AND (f.iid = :iid OR f.otherName = :otherName) AND f.expirationDate = :expirationDate")
    Optional<Fridge> findByUidAndIidOrOtherNameAndExpirationDate(Long uid, Long iid, String otherName, LocalDateTime expirationDate);

    Iterable<Fridge> findByUid(@NotNull(message = "uid cannot be null") Long uid);
}
