package com.example.Library.controller;

import com.example.Library.model.dto.BookCopyDto;
import com.example.Library.service.BookCopyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/bookCopy")
public class BookCopyController {

    private final BookCopyService bookCopyService;

    public BookCopyController(BookCopyService bookCopyService) {
        this.bookCopyService = bookCopyService;
    }

    @PostMapping(value = "/book/{bookId}")
    public ResponseEntity<BookCopyDto> createBookCopy(@PathVariable Long bookId,
                                                      @RequestBody BookCopyDto bookCopyDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookCopyService.createBookCopy(bookId, bookCopyDto));
    }

    @GetMapping(path = "/{bookCopyId}")
    public ResponseEntity<BookCopyDto> getBookCopy(@PathVariable Long bookCopyId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookCopyService.getBookCopy(bookCopyId));
    }

    @GetMapping
    public ResponseEntity<List<BookCopyDto>> getBookCopies() {
        return ResponseEntity.status(HttpStatus.OK).body(bookCopyService.getBookCopies());
    }

    @DeleteMapping(path = "/{bookCopyId}")
    public ResponseEntity<Void> deleteBookCopy(@PathVariable Long bookCopyId) {
        bookCopyService.delete(bookCopyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(path = "/{bookCopyId}")
    public ResponseEntity<BookCopyDto> updateBookCopy(@PathVariable Long bookCopyId,
                                                      @RequestBody BookCopyDto bookCopyDto) {
        return ResponseEntity.status(HttpStatus.OK).body(bookCopyService.updateBookCopy(bookCopyId, bookCopyDto));
    }

}
