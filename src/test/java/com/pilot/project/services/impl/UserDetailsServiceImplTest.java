package com.pilot.project.services.impl;

import com.pilot.project.entities.User;
import com.pilot.project.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Mock
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setName("Test User");
        user.setPassword("password");
        user.setEmail("test@email.com");
        user.setAbout("Test About");
    }

    @Test
    void loadUserByUsername_UserExists() {
        when(this.userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(user.getEmail());
        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
    }
    @Test
    void loadUserByUsername_UserDoesNotExist() {
        when(this.userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,this::loadUserByUsernameForTest);
    }
    void loadUserByUsernameForTest() {
        this.userDetailsServiceImpl.loadUserByUsername(user.getEmail());
    }
    @Test
    void loadUserByUsername_NullUser() {
        when(this.userRepository.findUserByEmail("xyz@gmail.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, this::loadUserByUsernameForTest_NullUser);
    }
    private void loadUserByUsernameForTest_NullUser() {
        this.userDetailsServiceImpl.loadUserByUsername("xyz@gmail.com");
    }
}