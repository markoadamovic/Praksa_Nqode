package com.example.Library.service.auth;

import com.example.Library.configuration.auth.JwtProvider;
import com.example.Library.exception.BadRequestException;
import com.example.Library.exception.NotFoundException;
import com.example.Library.exception.UnauthorizedException;
import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import com.example.Library.model.dto.auth.AuthRequest;
import com.example.Library.model.dto.auth.AuthResponse;
import com.example.Library.model.entity.AuthToken;
import com.example.Library.model.entity.User;
import com.example.Library.model.mapper.UserMapper;
import com.example.Library.repository.AuthTokenRepository;
import com.example.Library.repository.UserRepository;
import com.example.Library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Set;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

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
            return generateNewAuthToken(user,authRequest);
        }
    }

    private AuthResponse generateNewAuthToken(User user, AuthRequest authRequest) {
        String accessToken = jwtProvider.generateToken(user.getEmail(), user.getUserType().getName());
        saveAuthToken(authRequest, accessToken);
        return AuthResponse.builder().token(accessToken).build();
    }

    private AuthResponse generateAuthTokenFromExistingToken(User user) {
        AuthToken authToken = authTokenService.getAuthToken(user);
        if (!jwtProvider.isTokenExpired(authToken.getAccessToken())) {
            return AuthResponse.builder().token(authToken.getAccessToken()).build();
        }
        String accessToken = jwtProvider.generateToken(user.getEmail(), user.getUserType().getName());
        authToken.setAccessToken(accessToken);
        authTokenService.save(authToken);
        return AuthResponse.builder().token(accessToken).build();
    }

    public void saveAuthToken(AuthRequest request, String accessToken) {
        if (request.getEmail().isBlank() || request.getPassword().isBlank()) {
            throw new BadRequestException("Invalid credentials");
        }
        User user = findUserByEmail(request.getEmail());
        AuthToken authToken = new AuthToken();
        authToken.setUser(user);
        authToken.setAccessToken(accessToken);
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
        return userRepository.findByEmail(email).isPresent();
    }

    public void singout() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getUserByEmail(email);
        AuthToken authToken = authTokenService.getAuthToken(user);
        authTokenService.delete(authToken);
    }

}
