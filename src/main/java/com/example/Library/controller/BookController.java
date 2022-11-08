package com.example.Library.controller;

import com.example.Library.model.dto.BookDto;
import com.example.Library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/author/{authorId}/book")
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody BookDto bookDto,
                                        @PathVariable Long authorId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookDto, authorId));
    }

    @GetMapping
    public ResponseEntity<?> getBooks() {
        List<BookDto> bookList = bookService.getBooks();
        return new ResponseEntity<List<BookDto>>(bookList, HttpStatus.OK);
    }

    @GetMapping(path = "/{bookId}")
    public ResponseEntity<?> getBook(@PathVariable Long bookId) {
        BookDto bookDto = bookService.getBook(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(bookDto);
    }

    @DeleteMapping(path = "/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        bookService.delete(bookId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/{bookId}")
    public ResponseEntity<?> updateBook(@RequestBody BookDto bookDto,
                                        @PathVariable Long bookId) {
        BookDto bookDto1 = bookService.updateBook(bookDto, bookId);
        return ResponseEntity.status(HttpStatus.OK).body(bookDto1);
    }

}
