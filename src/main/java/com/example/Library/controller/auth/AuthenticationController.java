package com.example.Library.controller.auth;

import com.example.Library.configuration.auth.JwtProvider;
import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import com.example.Library.model.dto.auth.AuthRequest;
import com.example.Library.model.dto.auth.AuthResponse;
import com.example.Library.service.auth.AuthService;
import com.example.Library.service.auth.AuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    private final AuthTokenService authTokenService;

    private JwtProvider jwtProvider;

    @GetMapping("/authenticate/user")
    public ResponseEntity<UserDto> getAuthenticatedUser() {
        return ResponseEntity.status(HttpStatus.OK).body(authService.getAuthenticatedUser());
    }

    @PostMapping(path = "/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Validated AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping(path = "/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.ok(authService.register(userCreateDto));
    }

    @PostMapping(path = "/signout")
    public ResponseEntity logout() {
        authService.singout();
        return ResponseEntity.ok().build();
    }

}
