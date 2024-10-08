package com.pilot.project.configs;

import com.pilot.project.components.JwtTokenHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtTokenHelper jwtTokenHelper;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;


    @Test
    void doFilterInternal() throws ServletException, IOException {
        String token = "Bearer validToken";
        String username = "testUser";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtTokenHelper.getUsernameFromToken("validToken")).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtTokenHelper.validateToken("validToken", userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
        verify(jwtTokenHelper).getUsernameFromToken("validToken");
        verify(userDetailsService).loadUserByUsername(username);
        verify(jwtTokenHelper).validateToken("validToken", userDetails);
    }
    @Test
    void doFilterInternal_WithIllegalArgumentException()  {
        String token = "Bearer invalidToken";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtTokenHelper.getUsernameFromToken("invalidToken")).thenThrow(new IllegalArgumentException("Unable to get JWT Token"));
        assertThrows(IllegalArgumentException.class, () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
    }
    @Test
    void doFilterInternal_WithExpiredJwtException() {
        String token = "Bearer invalidToken";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtTokenHelper.getUsernameFromToken("invalidToken")).thenThrow(new ExpiredJwtException(null, null, "Expired JWT Token"));
        assertThrows(ExpiredJwtException.class, () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
    }
    @Test
    void doFilterInternal_WithMalformedJwtException() {
        String token = "Bearer invalidToken";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtTokenHelper.getUsernameFromToken("invalidToken")).thenThrow(new MalformedJwtException("Malformed JWT Token"));
        assertThrows(MalformedJwtException.class, () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));

    }
    @Test
    void doFilterInternal_NullUserDetails() throws ServletException, IOException {
        String token = "Bearer invalidToken";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtTokenHelper.getUsernameFromToken("invalidToken")).thenReturn(null);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        assertNull(jwtTokenHelper.getUsernameFromToken("invalidToken"));
    }
}