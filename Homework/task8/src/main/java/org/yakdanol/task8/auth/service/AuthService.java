package org.yakdanol.task8.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yakdanol.task8.auth.dto.LoginRequest;
import org.yakdanol.task8.auth.dto.RegisterRequest;
import org.yakdanol.task8.auth.model.Role;
import org.yakdanol.task8.auth.model.User;
import org.yakdanol.task8.auth.repository.RoleRepository;
import org.yakdanol.task8.auth.repository.UserRepository;
import org.yakdanol.task8.auth.util.JwtUtil;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void register(RegisterRequest request) {
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(userRole)) // Назначаем роль USER
                .build();

        userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return jwtUtil.generateToken(user.getUsername(), 600000);
        }
        throw new RuntimeException("Invalid credentials");
    }
}
