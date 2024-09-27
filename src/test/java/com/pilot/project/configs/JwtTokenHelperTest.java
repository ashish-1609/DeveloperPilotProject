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
        String token = this.jwtTokenHelper.generateToken(userDetails);
        String usernameFromToken = this.jwtTokenHelper.getUsernameFromToken(token);
        assertNotNull(usernameFromToken);
        assertEquals("test", usernameFromToken);
    }

    @Test
    void getExpirationDateFromToken() {
        String token = this.jwtTokenHelper.generateToken(userDetails);
        Date expirationDateFromToken = this.jwtTokenHelper.getExpirationDateFromToken(token);
        assertNotNull(expirationDateFromToken);
        assertTrue(expirationDateFromToken.after(new Date()));
    }

    @Test
    void generateToken() {
        String token = this.jwtTokenHelper.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void validateToken() {
        String token = this.jwtTokenHelper.generateToken(userDetails);
        String usernameFromToken = this.jwtTokenHelper.getUsernameFromToken(token);
        assertNotNull(usernameFromToken);
        assertEquals("test", usernameFromToken);
        assertEquals(userDetails.getUsername(), usernameFromToken);
        assertTrue(this.jwtTokenHelper.getExpirationDateFromToken(token).after(new Date()));
    }
}