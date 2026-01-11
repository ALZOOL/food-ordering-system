package com.example.foodordering.user.service;

import com.example.foodordering.user.dto.UserRegisterRequest;
import com.example.foodordering.user.dto.UserResponse;
import com.example.foodordering.user.entity.User;
import com.example.foodordering.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // new user registration
    public UserResponse register(UserRegisterRequest request) {

        // 1) email check
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // 2) create User Entity
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // Encrypt later
        user.setRole(request.getRole());
        user.setCreatedAt(LocalDateTime.now());

        // 3) save user to DB
        User savedUser = userRepository.save(user);

        // 4) convert entity to DTO
        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt()
        );
    }
}
