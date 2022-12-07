package com.example.Library.service.auth;

import com.example.Library.configuration.auth.JwtProvider;
import com.example.Library.exception.BadRequestException;
import com.example.Library.exception.NotFoundException;
import com.example.Library.exception.UnauthorizedException;
import com.example.Library.model.auth.*;
import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import com.example.Library.model.entity.User;
import com.example.Library.model.mapper.UserMapper;
import com.example.Library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final AuthTokenService authTokenService;

    public UserDto getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return UserMapper.toDto(userService.getUserByEmail(email));
    }

    public User getAuthenticatedUserModel() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return userService.getUserByEmail(email);
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        if (authRequest.getEmail().isBlank() || authRequest.getPassword().isBlank()) {
            throw new BadRequestException("Invalid credentials");
        }
        User user = findUserByEmail(authRequest.getEmail());
        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }
        if (authTokenService.userHasAccessToken(user)) {
            return generateAuthTokenFromExistingToken(user);
        } else {
            return generateNewAuthToken(user);
        }
    }

//    private AuthResponse generateNewAuthToken(User user) {
//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
//        String accessToken = jwtProvider.generateToken(user.getEmail(), user.getUserType().getName());
//        saveAuthToken(user, accessToken);
//        return AuthResponse.builder().jwtToken(accessToken).refreshToken(refreshToken.getRefreshToken()).build();
//    }

    private AuthResponse generateNewAuthToken(User user) {
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail(), user.getUserType().getName());
        String accessToken = jwtProvider.generateToken(user.getEmail(), user.getUserType().getName());
        saveAuthToken(user, accessToken, refreshToken);
        return AuthResponse.builder().jwtToken(accessToken).refreshToken(refreshToken).build();
    }

    private AuthResponse generateAuthTokenFromExistingToken(User user) {
        AuthToken authToken = authTokenService.getAuthTokenByUser(user);
        if (!jwtProvider.isTokenExpired(authToken.getAccessToken())) {
            return AuthResponse.builder()
                    .jwtToken(authToken.getAccessToken()).refreshToken(authToken.getRefreshToken()).build();
        }
        if (!jwtProvider.isTokenExpired(authToken.getRefreshToken())) {
            String newAccessToken = generateNewAccesToken(user);
            return AuthResponse.builder()
                    .jwtToken(newAccessToken).refreshToken(authToken.getRefreshToken()).build();
        }
        String newAccessToken = generateNewAccesToken(user);
        String newRefreshToken = generateNewRefreshToken(user);

        return AuthResponse.builder().jwtToken(newAccessToken).refreshToken(newRefreshToken).build();
    }

//    private boolean refreshTokenExpired(RefreshToken refreshToken) {
//        return refreshTokenService.refreshTokenExpired(refreshToken);
//    }

    private String generateNewAccesToken(User user) {
        AuthToken authToken = authTokenService.getAuthTokenByUser(user);
        String accessToken = jwtProvider.generateToken(user.getEmail(), user.getUserType().getName());
        authToken.setAccessToken(accessToken);
        authTokenService.save(authToken);

        return accessToken;
    }

    private String generateNewRefreshToken(User user) {
        AuthToken authToken = authTokenService.getAuthTokenByUser(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user.getEmail(), user.getUserType().getName());
        authToken.setRefreshToken(newRefreshToken);
        authTokenService.save(authToken);

        return newRefreshToken;
    }

    public void saveAuthToken(User user, String accessToken, String refreshToken) {
        if (user.getEmail().isBlank() || user.getPassword().isBlank()) {
            throw new BadRequestException("Invalid credentials");
        }
        AuthToken authToken = new AuthToken();
        authToken.setUser(user);
        authToken.setAccessToken(accessToken);
        authToken.setRefreshToken(refreshToken);
        authTokenService.save(authToken);
    }

    private User findUserByEmail(String email) {
        try {
            return userService.getUserByEmail(email);
        } catch (NotFoundException e) {
            throw new BadRequestException("Invalid credentials");
        }
    }

    public boolean hasAccess(Set<String> allowedRoles) {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getUserByEmail(email);
        return allowedRoles.contains(user.getUserType().getName());
    }

    public UserDto register(UserCreateDto userCreateDto) {
        if (userWithEmailExists(userCreateDto.getEmail())) {
            throw new BadRequestException(String.format("User with email %s  exists", userCreateDto.getEmail()));
        }
        User user = UserMapper.toEntity(userCreateDto);
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));

        return UserMapper.toDto(userService.saveUser(user));
    }

    public boolean userWithEmailExists(String email) {
        return userService.userWithEmailExists(email);
    }

    public void singout() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getUserByEmail(email);
        AuthToken authToken = authTokenService.getAuthTokenByUser(user);
        authTokenService.delete(authToken);
    }

    public AuthResponse refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        if (tokenRefreshRequest.getRefreshToken().isBlank()) {
            throw new BadRequestException("Invalid credentials");
        }
        String token = tokenRefreshRequest.getRefreshToken();
        AuthToken authToken = authTokenService.getAuthTokenByRefreshToken(token);
        if(authTokenService.refreshTokenExpired(authToken)){
            throw new UnauthorizedException("Not authorized");
        }
        User user = authToken.getUser();
        String jwtToken = jwtProvider.generateToken(user.getEmail(), user.getUserType().getName());
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail(), user.getUserType().getName());
        authToken.setAccessToken(jwtToken);
        authToken.setRefreshToken(refreshToken);
        authTokenService.save(authToken);
        return AuthResponse.builder().jwtToken(jwtToken).refreshToken(refreshToken).build();
    }

}
