package com.example.Library.controller;

import com.example.Library.model.dto.AuthorDto;
import com.example.Library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(authorDto));
    }

    @GetMapping(path = "/{authorId}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long authorId) {
        AuthorDto authorDto = authorService.getAuthor(authorId);

        return ResponseEntity.status(HttpStatus.OK).body(authorDto);
    }

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAuthors() {
        List<AuthorDto> authorsDto = authorService.getAuthors();

        return ResponseEntity.status(HttpStatus.OK).body(authorsDto);
    }

    @DeleteMapping(value = "/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
        authorService.delete(authorId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(path = "/{authorId}")
    public ResponseEntity<AuthorDto> updateAuthor(@RequestBody AuthorDto authorDto,
                                                  @PathVariable Long authorId) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.updateAuthor(authorDto, authorId));
    }

}
