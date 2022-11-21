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

    @PostMapping(value = "/{bookId}/{identification}")
    public ResponseEntity<BookCopyDto> createBookCopy(@PathVariable Long bookId,
                                                      @PathVariable String identification) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookCopyService.createBookCopy(bookId, identification));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<BookCopyDto> getBookCopy(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookCopyService.getBookCopy(id));
    }

    @GetMapping
    public ResponseEntity<List<BookCopyDto>> getBookCopies() {
        return ResponseEntity.status(HttpStatus.OK).body(bookCopyService.getBookCopies());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteBookCopy(@PathVariable Long id) {
        bookCopyService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<BookCopyDto> updateBookCopy(@PathVariable Long id,
                                                      @RequestBody BookCopyDto bookCopyDto) {
        return ResponseEntity.status(HttpStatus.OK).body(bookCopyService.updateBookCopy(id, bookCopyDto));
    }

}
