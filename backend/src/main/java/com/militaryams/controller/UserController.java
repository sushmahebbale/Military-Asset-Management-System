package com.militaryams.controller;

import com.militaryams.backend.entity.User;
import com.militaryams.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        users.forEach(user -> user.setPassword(null));

        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        User savedUser = userRepository.save(user);
        savedUser.setPassword(null);

        return savedUser;
    }

    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable Long id,
            @RequestBody User user) {

        User existingUser =
                userRepository.findById(id).orElseThrow();

        existingUser.setUsername(user.getUsername());
        existingUser.setFullName(user.getFullName());
        existingUser.setRole(user.getRole());
        existingUser.setAssignedBase(user.getAssignedBase());

        if (user.getPassword() != null
                && !user.getPassword().isEmpty()) {

            existingUser.setPassword(
                    passwordEncoder.encode(user.getPassword())
            );
        }

        User savedUser = userRepository.save(existingUser);
        savedUser.setPassword(null);

        return savedUser;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}