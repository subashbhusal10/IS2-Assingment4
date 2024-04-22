package com.hidemessage.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsServiceImpl userDetailsService;

    private final String[] permittedUrls;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();


    @Autowired
    public JwtFilter(
            JwtService jwtService,
            UserDetailsServiceImpl userDetailsService,
            @Qualifier("permittedUrls") String[] permittedUrls
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.permittedUrls = permittedUrls;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (Arrays.stream(permittedUrls).anyMatch(url -> pathMatcher.match(url, request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.error("Auth header is not present or Bearer is not present.");
            response.setHeader("message", "Auth header is not present or Bearer is not present.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String jwt = authHeader.replace("Bearer ", "");
            Claims parsedJwt = jwtService.validateToken(jwt);
            String subject = parsedJwt.getSubject();

            UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredJwtException ex) {
            logger.error("JWT Token is expired: ", ex);
            response.setHeader("message", "Jwt token is expired.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (JwtException ex) {
            logger.error("JWT Token not valid: ", ex);
            response.setHeader("message", "Jwt token is not valid.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (UsernameNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        filterChain.doFilter(request, response);
        this.resetAuthenticationAfterRequest();
    }

    private void resetAuthenticationAfterRequest() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }


}
