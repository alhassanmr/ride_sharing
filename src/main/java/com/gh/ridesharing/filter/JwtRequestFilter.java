package com.gh.ridesharing.filter;

import com.gh.ridesharing.service.UserDetailsServiceImpl;
import com.gh.ridesharing.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        log.info("Processing authentication for request {}", request.getRequestURI());

        Optional<String> optionalAuthorizationHeader = ofNullable(request.getHeader("Authorization"));

        if (!optionalAuthorizationHeader.isPresent() || !optionalAuthorizationHeader.get().startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = optionalAuthorizationHeader.get().substring(7);
        Optional<String> optionalUsername = ofNullable(jwtUtil.extractUsername(jwt));

        if (!optionalUsername.isPresent() || SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }

        log.info("Authenticated user {} setting security context", optionalUsername.get());

        UserDetails userDetails = userDetailsService.loadUserByUsername(optionalUsername.get());

        if (jwtUtil.validateToken(jwt)) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        chain.doFilter(request, response);
    }
}
