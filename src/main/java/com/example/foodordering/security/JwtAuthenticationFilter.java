package com.example.foodordering.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        //  reading Authorization Header
        String authHeader = request.getHeader("Authorization");

        //  checking Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            // check if token is valid
            if (JwtUtil.isTokenValid(token)) {

                // get email and role from token
                String email = JwtUtil.extractEmail(token);
                String role = JwtUtil.extractRole(token);

                // create Authentication Object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role))
                        );

                // connecting user to Spring Security Context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // continue filter chain
        filterChain.doFilter(request, response);
    }
}
