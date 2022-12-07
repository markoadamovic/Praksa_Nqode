package com.example.Library.service.auth;

import com.example.Library.configuration.auth.JwtProvider;
import com.example.Library.exception.BadRequestException;
import com.example.Library.exception.UnauthorizedException;
import com.example.Library.model.auth.AuthResponse;
import com.example.Library.model.auth.AuthToken;
import com.example.Library.model.auth.RefreshToken;
import com.example.Library.model.auth.TokenRefreshRequest;
import com.example.Library.model.entity.User;
import com.example.Library.repository.RefreshTokenRepository;
import com.example.Library.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserService userService;

    private final JwtProvider jwtProvider;

    private final AuthTokenService authTokenService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, AuthTokenService authTokenService,
                               JwtProvider jwtProvider, UserService userService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.authTokenService = authTokenService;
    }

    public Optional<RefreshToken> findByRefreshToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        User user = userService.findUserModel(userId);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setRefreshToken(jwtProvider.generateRefreshToken(user.getEmail(), user.getUserType().getName()));
        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public boolean refreshTokenExpired(RefreshToken refreshToken) {
        User user = refreshToken.getUser();
        AuthToken authToken = authTokenService.getAuthTokenByUser(user);
        if ((jwtProvider.isTokenExpired(refreshToken.getRefreshToken()))) {
            refreshTokenRepository.delete(refreshToken);
            authTokenService.delete(authToken);
            return true;
        }
        return false;
    }

    public RefreshToken getRefreshTokenByUser(User user) {
        return refreshTokenRepository.findByUser(user);
    }

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public AuthResponse refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        if (tokenRefreshRequest.getRefreshToken().isBlank()) {
            throw new BadRequestException("Invalid credentials");
        }
        String token = tokenRefreshRequest.getRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository
                .findByRefreshToken(token).orElseThrow(() -> new UnauthorizedException("Not authorized"));
        if(refreshTokenExpired(refreshToken)){
            throw new UnauthorizedException("Not authorized");
        }
        User user = refreshToken.getUser();
        AuthToken authToken = authTokenService.getAuthTokenByUser(user);
        String jwtToken = jwtProvider.generateToken(user.getEmail(), user.getUserType().getName());
        authToken.setAccessToken(jwtToken);
        authTokenService.save(authToken);
        return AuthResponse.builder().jwtToken(jwtToken).refreshToken(refreshToken.getRefreshToken()).build();
    }

}
