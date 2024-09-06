package com.end.fridge.controller;


import com.end.fridge.domain.User;
import com.end.fridge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/list")
    public ResponseEntity<?> list(){
        Iterable<User> users = userService.list();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> userData) {
        String email = userData.get("email");
        String password = userData.get("password");

        User user = new User(email, password);

        User u = userService.login(user);
        if (u != null) {
            return ResponseEntity.ok(u);
        } else {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userData) {
        String email = userData.get("email");
        String password = userData.get("password");
        String name = userData.get("name");
        User user = new User(email, password);
        user.setName(name);

        // Check if user already exists
        if (userService.findByEmail(email) != null) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("message", "User already exists with the given Email.");
            }});
        }

        // Save the new user
        userService.save(user);

        // Return a response entity
        return ResponseEntity.ok(new HashMap<String, String>() {{
            put("message", "User registered successfully!");
        }});
    }
}
