package com.example.Todo.service;

import com.example.Todo.dto.*;
import com.example.Todo.model.User;
import com.example.Todo.exception.*;
import com.example.Todo.repo.UserRepository;
import com.example.Todo.security.jwtService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private jwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUserFromJwt_ReturnsUser() {
        String token = "Bearer abc";
        when(jwtService.extractUsername("abc")).thenReturn("john");

        User user = new User();
        user.setUsername("john");

        when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));

        User result = userService.getUserFromJwt(token);

        assertEquals("john", result.getUsername());
    }

    @Test
    void testGetUserFromJwt_InvalidHeader() {
        String token = "InvalidToken";

        lenient().when(jwtService.extractUsername(token)).thenThrow(
            new RuntimeException("Invalid authorization header")
        );

        Exception exp = assertThrows(RuntimeException.class, () -> {
            userService.getUserFromJwt(token);
        });

        assertEquals("Invalid authorization header", exp.getMessage());
    }

    @Test
    void testGetUserFromJwt_UserNotFound() {
        String token = "Bearer abc";
        when(jwtService.extractUsername("abc")).thenReturn("john");

        when(userRepo.findByUsername("john")).thenReturn(Optional.empty());

        Exception exp = assertThrows(RuntimeException.class, () -> {
            userService.getUserFromJwt(token);
        });

        assertEquals("User not found", exp.getMessage());
    }


}
