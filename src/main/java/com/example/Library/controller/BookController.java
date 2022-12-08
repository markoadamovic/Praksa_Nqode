package com.example.Library.controller;

import com.example.Library.model.dto.BookDto;
import com.example.Library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(value = "/author/{authorId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto,
                                              @PathVariable Long authorId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookDto, authorId));
    }

    @GetMapping
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<List<BookDto>> getBooks() {
        List<BookDto> bookList = bookService.getBooks();

        return ResponseEntity.status(HttpStatus.OK).body(bookList);
    }

    @GetMapping(path = "/{bookId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<BookDto> getBook(@PathVariable Long bookId) {
        BookDto bookDto = bookService.getBook(bookId);

        return ResponseEntity.status(HttpStatus.OK).body(bookDto);
    }

    @DeleteMapping(path = "/{bookId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.delete(bookId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(path = "/{bookId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<BookDto> updateBook(@RequestBody BookDto bookDto,
                                              @PathVariable Long bookId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.updateBook(bookDto, bookId));
    }

}
