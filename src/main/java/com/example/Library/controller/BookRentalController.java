package com.example.Library.controller;

import com.example.Library.model.dto.BookRentalDto;
import com.example.Library.service.BookRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookRental")
public class BookRentalController {

    private final BookRentalService bookRentalService;

    @PostMapping(path ="/book/{bookId}/user/{userId}")
    public ResponseEntity<BookRentalDto> createBookRental(@PathVariable Long bookId,
                                                          @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookRentalService.createBookRental(bookId, userId));
    }

    @PostMapping(path = "/book/{bookId}/bookCopy/{bookCopyId}")
    public ResponseEntity<BookRentalDto> endBookRental(@PathVariable Long bookId,
                                                       @PathVariable Long bookCopyId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.endBookRental(bookId, bookCopyId));
    }

    @GetMapping
    public ResponseEntity<List<BookRentalDto>> getRentedBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.getRentedBooks());
    }

    @GetMapping(path = "/{bookRentalId}")
    public ResponseEntity<BookRentalDto> getRentedBook(@PathVariable Long bookRentalId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.getRentedBook(bookRentalId));
    }

    @PutMapping(path = "/{bookRentalId}")
    public ResponseEntity<BookRentalDto> updateRentedBook(@PathVariable Long bookRentalId,
                                                          @RequestBody BookRentalDto bookRentalDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookRentalService.updateRentedBook(bookRentalId, bookRentalDto));
    }

    @GetMapping(value = "/activeRents")
    public ResponseEntity<List<BookRentalDto>> getActiveRents() {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.getActiveRents());
    }

    @GetMapping(value = "/closedRents")
    public ResponseEntity<List<BookRentalDto>> getClosedRents() {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.getClosedRents());
    }

}
