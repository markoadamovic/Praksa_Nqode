package com.example.Library.controller.auth;

import com.example.Library.model.auth.TokenRefreshRequest;
import com.example.Library.model.dto.AuthorDto;
import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import com.example.Library.model.auth.AuthRequest;
import com.example.Library.model.auth.AuthResponse;
import com.example.Library.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;

    @Operation(
            description = "Returns the currently logged in user",
            operationId = "getAuthenticatedUser",
            summary = "Get authenticated user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully returned authenticated user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/getUser")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<UserDto> getAuthenticatedUser() {
        return ResponseEntity.status(HttpStatus.OK).body(authService.getAuthenticatedUser());
    }

    @Operation(
            description = "Login user with username and password",
            operationId = "authenticate",
            summary = "Authenticate",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully login",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid credentials",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping(path = "/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Validated AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @Operation(
            description = "Register new user",
            operationId = "register",
            summary = "Register",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully registered",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Username is already in use",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping(path = "/authenticate/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.ok(authService.register(userCreateDto));
    }

    @Operation(
            description = "Sign-out user, jwt token will be deleted",
            operationId = "logout",
            summary = "Logout",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Logout is successfully"
                    )
            }
    )
    @PostMapping(path = "/sign-out")
    public ResponseEntity<Void> logout() {
        authService.singout();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            description = "Refresh access token",
            operationId = "refreshToken",
            summary = "Refresh token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully refreshed",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Refresh token has been expired",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping(path = "/authenticate/refreshToken")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody @Validated TokenRefreshRequest tokenRefreshRequest) {
        return ResponseEntity.ok(authService.refreshToken(tokenRefreshRequest));
    }

}
