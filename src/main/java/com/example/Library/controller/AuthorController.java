package com.example.Library.controller;

import com.example.Library.model.dto.AuthorDto;
import com.example.Library.service.AuthorService;
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
@RequestMapping(value = "/author")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(
            description = "Create Author and save it to database",
            operationId = "createAuthor",
            summary = "Create author",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthorDto.class)))
            }
    )
    @PostMapping
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(authorDto));
    }

    @Operation(
            description = "Get author by provided ID",
            operationId = "getAuthor",
            summary = "Get author",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Author is successfully retrieved from database",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthorDto.class)) }),
                    @ApiResponse(responseCode = "404", description = "Author is not found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping(path = "/{authorId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long authorId) {
        AuthorDto authorDto = authorService.getAuthor(authorId);

        return ResponseEntity.status(HttpStatus.OK).body(authorDto);
    }

    @Operation(
            description = "Get list of authors",
            operationId = "getAuthors",
            summary = "Get authors",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authors are successfully returned",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AuthorDto.class))))
            }
    )
    @GetMapping
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<List<AuthorDto>> getAuthors() {
        List<AuthorDto> authorsDto = authorService.getAuthors();

        return ResponseEntity.status(HttpStatus.OK).body(authorsDto);
    }

    @Operation(
            description = "Delete author with provided ID",
            operationId = "deleteAuthor",
            summary = "Delete author",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Author is successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Author with provided id is not found"),
                    @ApiResponse(responseCode = "400", description = "Author can not be deleted because " +
                            "it is assigned to another entity")
            }
    )
    @DeleteMapping(value = "/{authorId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
        authorService.delete(authorId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            description = "Update author by provided ID",
            operationId = "updateAuthor",
            summary = "Update author",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthorDto.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The Author is not found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PutMapping(path = "/{authorId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<AuthorDto> updateAuthor(@RequestBody AuthorDto authorDto,
                                                  @PathVariable Long authorId) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.updateAuthor(authorDto, authorId));
    }

}
