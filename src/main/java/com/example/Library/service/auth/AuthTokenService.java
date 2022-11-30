package com.example.Library.service.auth;

import com.example.Library.exception.NotFoundException;
import com.example.Library.exception.UnauthorizedException;
import com.example.Library.model.entity.AuthToken;
import com.example.Library.model.entity.User;
import com.example.Library.repository.AuthTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenService {

    private AuthTokenRepository authTokenRepository;

    public AuthTokenService(AuthTokenRepository authTokenRepository) {
        this.authTokenRepository = authTokenRepository;
    }

    public AuthToken getAuthToken(User user) {
        return authTokenRepository.findByUserId(user.getId())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
    }

    public boolean userHasAccessToken(User user) {
        return authTokenRepository.userHasAccessToken(user);
    }

    public AuthToken save(AuthToken authToken) {
        return authTokenRepository.save(authToken);
    }

    public void delete(AuthToken authToken) {
        authTokenRepository.delete(authToken);
    }
}
