package com.pilot.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pilot.project.entities.User;
import com.pilot.project.payloads.UserDTO;
import com.pilot.project.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder()
                .userId(UUID.randomUUID().toString())
                .name("Test User")
                .password("password")
                .about("Test About")
                .build();

        user = new User();
        user.setUserId(userDTO.getUserId());
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());
        user.setAbout(userDTO.getAbout());
    }

    @AfterEach
    void tearDown() {
        System.gc();
    }

    @Test
    void saveUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void deleteUser() {
    }
}