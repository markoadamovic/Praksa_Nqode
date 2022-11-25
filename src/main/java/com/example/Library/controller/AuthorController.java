package com.example.Library.controller;

import com.example.Library.model.dto.AuthorDto;
import com.example.Library.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/author")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    @PostMapping
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(authorDto));
    }

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
