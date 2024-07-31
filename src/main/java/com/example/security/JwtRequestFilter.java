package com.example.security;

import com.example.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Check if the Authorization header is present and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Extract token
            try {
                username = jwtUtil.extractUsername(jwt); // Extract username from token
            } catch (ExpiredJwtException e) {
                // Token is expired, send 401 Unauthorized response
                logger.warn("JWT Token has expired: " + e.getMessage());
                response.setStatus(401); // Set HTTP status 401
                response.getWriter().write("JWT Token has expired");
                return; // Stop further processing
            } catch (Exception e) {
                // Handle other possible exceptions
                logger.error("JWT Token processing error: " + e.getMessage());
                response.setStatus(403); // Set HTTP status 403
                response.getWriter().write("JWT Token processing error");
                return; // Stop further processing
            }
        }

        // If the username is extracted from token and there is no existing authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // If token is valid, set authentication in security context
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                // Token is invalid, send 403 Forbidden response
                logger.warn("Invalid JWT Token");
                response.setStatus(403); // Set HTTP status 403
                response.getWriter().write("Invalid JWT Token");
                return; // Stop further processing
            }
        }

        // Continue with the request if token is valid
        chain.doFilter(request, response);
    }
}
