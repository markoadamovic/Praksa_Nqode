package com.example.Library.service.auth;

import com.example.Library.configuration.auth.JwtProvider;
import com.example.Library.exception.BadRequestException;
import com.example.Library.exception.NotFoundException;
import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import com.example.Library.model.dto.auth.AuthRequest;
import com.example.Library.model.dto.auth.AuthResponse;
import com.example.Library.model.entity.User;
import com.example.Library.model.mapper.UserMapper;
import com.example.Library.repository.UserRepository;
import com.example.Library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    public UserDto getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return UserMapper.toDto(userService.getUserByEmail(email));
    }

    public User getAuthenticatedUserModel() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return userService.getUserByEmail(email);
    }

    public AuthResponse authenticate(AuthRequest request) {
        if(request.getEmail().isBlank() || request.getPassword().isBlank()) {
            throw new BadRequestException("Invalid credentials");
        }
        User user = findUserByEmail(request.getEmail());
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }
        return AuthResponse.builder().token(jwtProvider.generateToken(user.getEmail(), user.getPassword())).build();
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
        if(userWithEmailExists(userCreateDto.getEmail())) {
            throw new BadRequestException(String.format("User with email %s  exists", userCreateDto.getEmail()));
        }
        User user = UserMapper.toEntity(userCreateDto);
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));

        return UserMapper.toDto(userRepository.save(user));
    }

    public boolean userWithEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
