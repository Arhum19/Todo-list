package com.example.Todo.controller;

import com.example.Todo.model.User;
import com.example.Todo.repo.UserRepository;
import com.example.Todo.security.jwtService;
import com.example.Todo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)   // Disable Spring Security Filters
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository repo;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private jwtService jwtService;

    @MockBean
    private UserService userService;

    @Test
    void Test_FailWhen_UsernameExists() throws Exception {
        when(repo.existsByUsername("arhum")).thenReturn(true);

        String body = """
                {
                  "username": "arhum",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Username is already taken!"));
    }

    @Test
    void Test_SuccessWhen_NewUsername() throws Exception {
        when(repo.existsByUsername("newuser")).thenReturn(false);
        when(encoder.encode("password123")).thenReturn("encodedPassword");

        String body = """
                {
                  "username": "newuser",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void Test_ReturnValidCredentialsWhen_Login() throws Exception {
        String token = "mocked-jwt-token";

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("arhum");
        mockUser.setPassword("password123");

        when(repo.findByUsername("arhum")).thenReturn(Optional.of(mockUser));
        when(encoder.matches("password123", mockUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken("arhum")).thenReturn(token);

        String body = """
                {
                  "username": "arhum",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.username").value("arhum"));
    }

    @Test
    void Test_FailWhen_InvalidLogin() throws Exception {
        when(repo.findByUsername("arhum")).thenReturn(Optional.empty());

        String body = """
                {
                  "username": "arhum",
                  "password": "wrongpassword"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void Test_FailWhen_InvalidAdminSecret() throws Exception {
        String body = """
                {
                  "username": "adminuser",
                  "password": "adminpass"
                }
                """;

        mockMvc.perform(post("/api/auth/admin/signup")
                        .with(csrf())
                        .param("adminSecret", "wrongsecret")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Invalid admin secret"));
    }

    @Test
    void Test_SuccessWhen_ValidAdminSignup() throws Exception {
        when(repo.existsByUsername("adminuser")).thenReturn(false);
        when(encoder.encode("adminpass")).thenReturn("encodedAdminPass");

        String body = """
                {
                  "username": "adminuser",
                  "password": "adminpass"
                }
                """;

        mockMvc.perform(post("/api/auth/admin/signup")
                        .with(csrf())
                        .param("adminSecret", "1234567890")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().string("Admin user registered successfully"));
    }

    @Test
    void Check_admin_role_when_sigin() throws Exception{
        when(repo.existsByUsername("adminuser")).thenReturn(false);
        when(encoder.encode("adminpass")).thenReturn("encodedAdminPass");

        String body = """
                {
                  "username": "adminuser",
                  "password": "adminpass"
                }
                """;

        mockMvc.perform(post("/api/auth/admin/signup")
                        .with(csrf())
                        .param("adminSecret", "1234567890")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().string("Admin user registered successfully"));

        verify(repo, times(1)).save(argThat(user ->
            user.getRoles().contains("ROLE_ADMIN") && user.getRoles().contains("ROLE_USER")
        ));
    }


}
