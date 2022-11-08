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

    @Autowired
    AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(authorDto));
    }

    @GetMapping(path = "/{authorId}")
    public ResponseEntity<?> getAuthor(@PathVariable Long authorId) {
        AuthorDto authorDto = authorService.getAuthor(authorId);
        return ResponseEntity.status(HttpStatus.OK).body(authorDto);
    }

    @GetMapping
    public ResponseEntity<?> getAuthors() {
        List<AuthorDto> authorsDto = authorService.getAuthors();
        return new ResponseEntity<List<AuthorDto>>(authorsDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{authorId}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long authorId) {
        authorService.delete(authorId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{authorId}")
    public ResponseEntity<?> updateAuthor(@RequestBody AuthorDto authorDto,
                                          @PathVariable Long authorId) {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.updateAuthor(authorDto, authorId));
    }

}
