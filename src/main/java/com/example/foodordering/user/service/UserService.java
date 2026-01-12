package com.example.foodordering.user.service;

import com.example.foodordering.common.exception.ResourceNotFoundException;
import com.example.foodordering.user.dto.LoginRequest;
import com.example.foodordering.user.dto.LoginResponse;
import com.example.foodordering.user.dto.UserRegisterRequest;
import com.example.foodordering.user.dto.UserResponse;
import com.example.foodordering.user.entity.User;
import com.example.foodordering.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.foodordering.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // new user registration with password encryption
    public UserResponse register(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // encrypt password
        user.setRole(request.getRole());
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getCreatedAt()
        );
    }

    // login
    public LoginResponse login(LoginRequest request) {

    // check if user exists
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new ResourceNotFoundException("User", "email", request.getEmail())
            );

    // check password
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new RuntimeException("Invalid email or password");
    }

    // Generate JWT token
    String token = JwtUtil.generateToken(
            user.getEmail(),
            user.getRole().name()
    );

    // Prepare responseءءء
    LoginResponse response = new LoginResponse();
    response.setEmail(user.getEmail());
    response.setRole(user.getRole().name());
    response.setToken(token);

    return response;
    }
}
