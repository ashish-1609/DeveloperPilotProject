package com.pilot.project.configs;


import com.pilot.project.components.JwtTokenHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenHelperTest {

    @InjectMocks
    private JwtTokenHelper jwtTokenHelper;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        when(userDetails.getUsername()).thenReturn("test");
    }

    @Test
    void getUsernameFromToken() {
        String token = jwtTokenHelper.generateToken(userDetails);
        String usernameFromToken = jwtTokenHelper.getUsernameFromToken(token);
        assertNotNull(usernameFromToken);
        assertEquals("test", usernameFromToken);
    }

    @Test
    void getExpirationDateFromToken() {
        String token = jwtTokenHelper.generateToken(userDetails);
        Date expirationDateFromToken = jwtTokenHelper.getExpirationDateFromToken(token);
        assertNotNull(expirationDateFromToken);
        assertTrue(expirationDateFromToken.after(new Date()));
    }

    @Test
    void generateToken() {
        String token = jwtTokenHelper.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void validateToken() {
        String token = jwtTokenHelper.generateToken(userDetails);
        String usernameFromToken = jwtTokenHelper.getUsernameFromToken(token);
        assertNotNull(usernameFromToken);
        assertEquals("test", usernameFromToken);
        assertEquals(userDetails.getUsername(), usernameFromToken);
        assertTrue(jwtTokenHelper.getExpirationDateFromToken(token).after(new Date()));
    }
}