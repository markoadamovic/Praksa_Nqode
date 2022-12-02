package com.example.Library.controller.auth;

import com.example.Library.model.auth.TokenRefreshRequest;
import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import com.example.Library.model.auth.AuthRequest;
import com.example.Library.model.auth.AuthResponse;
import com.example.Library.service.auth.AuthService;
import com.example.Library.service.auth.RefreshTokenService;
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

    private final RefreshTokenService refreshTokenService;

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

    @PostMapping(path = "/signOut")
    public ResponseEntity<Void> logout() {
        authService.singout();
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/refreshToken")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody @Validated TokenRefreshRequest tokenRefreshRequest) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(tokenRefreshRequest));
    }

}
