package com.pilot.project.configs;

import com.pilot.project.components.JwtTokenHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenHelper jwtTokenHelper;

    @Autowired
    public JwtAuthenticationFilter(@Lazy UserDetailsService userDetailsService, JwtTokenHelper jwtTokenHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            try {
                username=jwtTokenHelper.getUsernameFromToken(jwtToken);
            }
            catch(IllegalArgumentException e) {
                logger.error("Unable to get JWT Token");
                throw new IllegalArgumentException("Unable to get JWT Token");
            }
            catch(ExpiredJwtException e) {
                logger.error("Expired JWT Token");
                throw new ExpiredJwtException(null,null,"Expired JWT Token");
            }
            catch(MalformedJwtException e) {
                logger.error("Malformed JWT Token");
                throw new MalformedJwtException("Malformed JWT Token");
            }
        }
        if(username!= null && SecurityContextHolder.getContext().getAuthentication()==null) {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            if(Boolean.TRUE.equals(jwtTokenHelper.validateToken(jwtToken,user))) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
            else {
                logger.error("Invalid JWT Token ");
            }

        }
        else {
            logger.error("Username is null or context is not null");
        }
        filterChain.doFilter(request, response);
    }
}
