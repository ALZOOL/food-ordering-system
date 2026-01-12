package com.example.foodordering.config;

import com.example.foodordering.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // 1️⃣ dont use Sessions
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // disable CSRF bcz we use REST API
            .csrf(csrf -> csrf.disable())

            // 3️⃣ Authorization rules
            .authorizeHttpRequests(auth -> auth

                // Public APIs
                .requestMatchers("/api/users/register", "/api/users/login")
                .permitAll()

                // Menu - GET for all roles
                .requestMatchers("/api/categories/**", "/api/menu-items/**")
                .hasAnyRole("CUSTOMER", "EMPLOYEE", "MANAGER")

                // anything else
                .anyRequest()
                .hasRole("MANAGER")
            )

            // 4️⃣ add JWT Filter
            .addFilterBefore(
                new JwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class
            )

            // 5️⃣ basic config
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
