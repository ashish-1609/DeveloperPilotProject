package com.pilot.project.payloads;

import com.pilot.project.entities.Role;
import com.pilot.project.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsTest {

    private CustomUserDetails customUserDetails;

    private User user;
    Role role1;
    Role role2;


    @BeforeEach
    void setUp() {


        user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("Test User");
        user.setPassword("password");
        user.setEmail("test@email.com");
        user.setAbout("Test About");
        user.setRoles(new HashSet<>());
        customUserDetails = new CustomUserDetails(user);
    }

    @Test
    void getUsername(){
        String username = customUserDetails.getUsername();
        assertNotNull(username);
        assertEquals(user.getEmail(), username);
    }

    @Test
    void getPassword(){
        String password = customUserDetails.getPassword();
        assertNotNull(password);
        assertEquals(user.getPassword(), password);

    }

    @Test
    void getAuthorities_WithMultipleRoles_ReturnsAuthorities() {
        role1 = mock(Role.class);
        role2 = mock(Role.class);
        user.setRoles(Set.of(role1, role2));
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();

        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(role1.toString())));
    }
    @Test
    void getAuthorities_WithNoRoles_ReturnsEmptyAuthorities() {
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        assertTrue(authorities.isEmpty());
    }

}