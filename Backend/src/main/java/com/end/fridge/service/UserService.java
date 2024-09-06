package com.end.fridge.service;

import com.end.fridge.domain.User;
import com.end.fridge.repository.UserRepository;
import com.end.fridge.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    public Iterable<User> list() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void save(List<User> users) {
        userRepository.saveAll(users);
    }

    public User login(User user) {

        User foundUser = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

        if (foundUser != null) {
            foundUser.setToken(jwtUtil.generateToken(foundUser.getName()));
            return foundUser;
        } else {
            return null;
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
