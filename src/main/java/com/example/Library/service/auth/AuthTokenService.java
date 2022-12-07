package com.example.Library.service.auth;

import com.example.Library.configuration.auth.JwtProvider;
import com.example.Library.exception.BadRequestException;
import com.example.Library.exception.UnauthorizedException;
import com.example.Library.model.auth.AuthResponse;
import com.example.Library.model.auth.AuthToken;
import com.example.Library.model.auth.TokenRefreshRequest;
import com.example.Library.model.entity.User;
import com.example.Library.repository.AuthTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenService {

    private AuthTokenRepository authTokenRepository;

    private final JwtProvider jwtProvider;

    public AuthTokenService(AuthTokenRepository authTokenRepository, JwtProvider jwtProvider) {
        this.authTokenRepository = authTokenRepository;
        this.jwtProvider = jwtProvider;
    }

    public AuthToken getAuthTokenByUser(User user) {
        return authTokenRepository.findByUser(user.getId())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
    }

    public boolean userHasAccessToken(User user) {
        return authTokenRepository.userHasAccessToken(user.getId());
    }

    public AuthToken save(AuthToken authToken) {
        return authTokenRepository.save(authToken);
    }

    public void delete(AuthToken authToken) {
        authTokenRepository.delete(authToken);
    }

    public AuthToken getAuthTokenByRefreshToken(String refreshToken) {
        return authTokenRepository
                .findByRefreshToken(refreshToken).orElseThrow(() -> new UnauthorizedException("Not authorized"));
    }

    public boolean refreshTokenExpired(AuthToken authToken) {
        if ((jwtProvider.isTokenExpired(authToken.getRefreshToken()))) {
            delete(authToken);
            return true;
        }
        return false;
    }

}
