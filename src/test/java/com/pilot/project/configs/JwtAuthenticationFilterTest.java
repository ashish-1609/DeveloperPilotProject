package com.pilot.project.configs;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
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

    Logger logger = LogManager.getLogger(JwtAuthenticationFilterTest.class);

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
    void doFilterInternal_WithIllegalArgumentException() throws ServletException, IOException {
        String token = "Bearer invalidToken";
        String username = "invalidUser";
        UserDetails userDetails = mock(UserDetails.class);

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtTokenHelper.getUsernameFromToken("invalidToken")).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtTokenHelper.validateToken("invalidToken", userDetails)).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
    }
    @Test
    void doFilterInternal_WithExpiredJwtException() throws ServletException, IOException {
        String token = "Bearer invalidToken";
        String username = "invalidUser";
        UserDetails userDetails = mock(UserDetails.class);
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtTokenHelper.getUsernameFromToken("invalidToken")).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtTokenHelper.validateToken("invalidToken", userDetails)).thenThrow(new ExpiredJwtException(null, null, token));
        assertThrows(ExpiredJwtException.class, () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
    }
    @Test
    void doFilterInternal_WithMalformedJwtException() throws ServletException, IOException {
        String token = "Bearer invalidToken";
        String username = "invalidUser";
        UserDetails userDetails = mock(UserDetails.class);
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtTokenHelper.getUsernameFromToken("invalidToken")).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtTokenHelper.validateToken("invalidToken", userDetails)).thenReturn(false);
        when(jwtTokenHelper.validateToken("invalidToken", userDetails)).thenThrow(new MalformedJwtException(token));
        assertThrows(MalformedJwtException.class, () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));

    }
    @Test
    void doFilterInternal_NullUserDetails() throws ServletException, IOException {
        String token = "Bearer invalidToken";
        String username;
        UserDetails userDetails = null;
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtTokenHelper.getUsernameFromToken("invalidToken")).thenReturn(null);
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}