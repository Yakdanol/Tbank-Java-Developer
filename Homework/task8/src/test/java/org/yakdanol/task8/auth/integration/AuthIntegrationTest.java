//package org.yakdanol.task8.auth.integration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.yakdanol.task8.auth.dto.LoginRequest;
//import org.yakdanol.task8.auth.dto.RegisterRequest;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@AutoConfigureMockMvc
//class AuthIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void testFullRegistrationAndLoginProcess() throws Exception {
//        RegisterRequest registerRequest = new RegisterRequest();
//        registerRequest.setUsername("integrationUser");
//        registerRequest.setPassword("integrationPassword");
//
//        mockMvc.perform(post("/api/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(registerRequest)))
//                .andExpect(status().isOk());
//
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setUsername("integrationUser");
//        loginRequest.setPassword("integrationPassword");
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk());
//    }
//}
//
