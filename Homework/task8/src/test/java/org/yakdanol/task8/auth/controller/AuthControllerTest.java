package org.yakdanol.task8.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.yakdanol.task8.auth.model.Role;
import org.yakdanol.task8.auth.model.User;
import org.yakdanol.task8.auth.repository.RoleRepository;
import org.yakdanol.task8.auth.repository.UserRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        Role userRole = new Role(null, "ROLE_USER");
        Role adminRole = new Role(null, "ROLE_ADMIN");
        roleRepository.save(userRole);
        roleRepository.save(adminRole);

        User user = User.builder()
                .username("testuser")
                .password(passwordEncoder.encode("password123"))
                .roles(Set.of(userRole))
                .build();
        userRepository.save(user);
    }

    @Test
    void testRegister() throws Exception {
        User newUser = User.builder()
                .username("newuser")
                .password("newpassword")
                .roles(Set.of(roleRepository.findByName("ROLE_USER").orElseThrow()))
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void testLogin() throws Exception {
        String loginRequest = """
            {
                "username": "testuser",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testGetUserProfile() throws Exception {
        mockMvc.perform(get("/api/auth/profile")
                        .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testResetPassword() throws Exception {
        String resetRequest = """
            {
                "username": "testuser",
                "newPassword": "newpassword123"
            }
            """;

        mockMvc.perform(post("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resetRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successful"));

        User updatedUser = userRepository.findByUsername("testuser").orElseThrow();
        assertTrue(passwordEncoder.matches("newpassword123", updatedUser.getPassword()));
    }
}
