package com.example.Library.controller;

import com.example.Library.model.dto.AuthorDto;
import com.example.Library.model.entity.Author;
import com.example.Library.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @PostMapping
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(authorDto));
    }

    @Operation(summary = "Get author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author is found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Author.class)) }),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content) })
    @GetMapping(path = "/{authorId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long authorId) {
        AuthorDto authorDto = authorService.getAuthor(authorId);

        return ResponseEntity.status(HttpStatus.OK).body(authorDto);
    }

    @GetMapping
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<List<AuthorDto>> getAuthors() {
        List<AuthorDto> authorsDto = authorService.getAuthors();

        return ResponseEntity.status(HttpStatus.OK).body(authorsDto);
    }

    @DeleteMapping(value = "/{authorId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
        authorService.delete(authorId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(path = "/{authorId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<AuthorDto> updateAuthor(@RequestBody AuthorDto authorDto,
                                                  @PathVariable Long authorId) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.updateAuthor(authorDto, authorId));
    }

}
