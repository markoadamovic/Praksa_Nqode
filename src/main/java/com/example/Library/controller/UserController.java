package com.example.Library.controller;

import com.example.Library.model.dto.*;
import com.example.Library.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            description = "Create User and save it to database",
            operationId = "createUser",
            summary = "Create user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)))
            }
    )
    @PostMapping
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<UserCreateDto> createUser(@RequestBody UserCreateDto userCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userCreateDto));
    }

    @Operation(
            description = "Get User by provided ID",
            operationId = "getUser",
            summary = "Get user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully returned",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "404", description = "User with provided ID is not found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping(path = "/{userId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userId));
    }

    @Operation(
            description = "Get list of users",
            operationId = "getUsers",
            summary = "Get users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully returned",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
            }
    )
    @GetMapping
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }

    @Operation(
            description = "Delete user with provided ID",
            operationId = "deleteUser",
            summary = "Delete user",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "User is not found"),
                    @ApiResponse(responseCode = "400", description = "User can not be deleted because" +
                            " it is assigned to another entity")
            }
    )
    @DeleteMapping(path = "/{userId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            description = "Update user with provided ID",
            operationId = "updateUser",
            summary = "Update user",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully updated",
                            content = @Content(mediaType = "application/json",
                                    schema =  @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "404", description = "User is not found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PutMapping(path = "/{userId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,
                                              @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userDto, userId));
    }

    @Operation(
            description = "Get rented books for user with provided ID",
            operationId = "getRentedBooksForUser",
            summary = "Get rented books",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully returned",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookRentalDto.class)))),
                    @ApiResponse(responseCode = "404", description = "User is not found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping(path = "/getRentals/{userId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<List<BookRentalDto>> getRentedBooksForUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getRentedBooks(userId));
    }

}