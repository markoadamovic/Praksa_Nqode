package com.example.Library.configuration.auth;

import com.example.Library.exception.UnauthorizedException;
import com.example.Library.model.auth.AuthToken;
import com.example.Library.model.entity.User;
import com.example.Library.service.auth.AuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthTokenService authTokenService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (!isNull(token) && !authenticated(token)) {
            throw new UnauthorizedException("Not authorized");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean authenticated(String token) {
        try {
            String email = jwtProvider.getEmailFromToken(token);
            User user = userDetailsService.getUserByEmail(email);
            AuthToken authToken = authTokenService.getAuthToken(user);
            if (!authToken.getAccessToken().equals(jwtProvider.removeBearerFromToken(token))) {
                return false;
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (Exception e) {
            // exception not relevant since unauthorized will be returned
            return false;
        }
    }

}
