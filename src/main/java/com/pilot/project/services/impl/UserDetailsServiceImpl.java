package com.pilot.project.services.impl;

import com.pilot.project.entities.User;
import com.pilot.project.payloads.CustomUserDetails;
import com.pilot.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username).orElseThrow(()-> new UsernameNotFoundException("User not found with username: "+username));
        if(user==null) throw new UsernameNotFoundException("User not found with username: "+username);
        return new CustomUserDetails(user);
    }
}
