package com.end.fridge.repository;

import com.end.fridge.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);

    User findByNameAndPassword(String name, String password);
    User findByEmailAndPassword(String Email, String password);
}
