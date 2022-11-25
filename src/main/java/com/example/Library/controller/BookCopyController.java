package com.example.Library.controller;

import com.example.Library.model.dto.BookCopyDto;
import com.example.Library.service.BookCopyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<BookCopyDto> createBookCopy(@PathVariable Long bookId,
                                                      @RequestBody BookCopyDto bookCopyDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookCopyService.createBookCopy(bookId, bookCopyDto));
    }

    @GetMapping(path = "/{bookCopyId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<BookCopyDto> getBookCopy(@PathVariable Long bookCopyId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookCopyService.getBookCopy(bookCopyId));
    }

    @GetMapping
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<List<BookCopyDto>> getBookCopies() {
        return ResponseEntity.status(HttpStatus.OK).body(bookCopyService.getBookCopies());
    }

    @DeleteMapping(path = "/{bookCopyId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<Void> deleteBookCopy(@PathVariable Long bookCopyId) {
        bookCopyService.delete(bookCopyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(path = "/{bookCopyId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<BookCopyDto> updateBookCopy(@PathVariable Long bookCopyId,
                                                      @RequestBody BookCopyDto bookCopyDto) {
        return ResponseEntity.status(HttpStatus.OK).body(bookCopyService.updateBookCopy(bookCopyId, bookCopyDto));
    }

}
