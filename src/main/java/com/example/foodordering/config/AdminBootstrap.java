package com.example.foodordering.config;

import com.example.foodordering.user.entity.Role;
import com.example.foodordering.user.entity.User;
import com.example.foodordering.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Component
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminBootstrap(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (!userRepository.existsByEmail("admin@system.com")) {

            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@system.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.MANAGER);
            admin.setCreatedAt(LocalDateTime.now());

            userRepository.save(admin);
        }
    }
}
