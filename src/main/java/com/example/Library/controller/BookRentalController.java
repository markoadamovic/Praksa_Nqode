package com.example.Library.controller;

import com.example.Library.model.dto.BookRentalDto;
import com.example.Library.service.BookRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookRental")
public class BookRentalController {

    private final BookRentalService bookRentalService;

    @PostMapping(path ="/book/{bookId}/user/{userId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<BookRentalDto> createBookRental(@PathVariable Long bookId,
                                                          @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookRentalService.createBookRental(bookId, userId));
    }

    @PostMapping(path = "/book/{bookId}/bookCopy/{bookCopyId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<BookRentalDto> endBookRental(@PathVariable Long bookId,
                                                       @PathVariable Long bookCopyId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.endBookRental(bookId, bookCopyId));
    }

    @GetMapping
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<List<BookRentalDto>> getRentedBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.getRentedBooks());
    }

    @GetMapping(path = "/{bookRentalId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<BookRentalDto> getRentedBook(@PathVariable Long bookRentalId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.getRentedBook(bookRentalId));
    }

    @PutMapping(path = "/{bookRentalId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<BookRentalDto> updateRentedBook(@PathVariable Long bookRentalId,
                                                          @RequestBody BookRentalDto bookRentalDto) {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.updateRentedBook(bookRentalId, bookRentalDto));
    }

    @GetMapping(value = "/activeRents")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<List<BookRentalDto>> getActiveRents() {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.getActiveRents());
    }

    @GetMapping(value = "/closedRents")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<List<BookRentalDto>> getClosedRents() {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.getClosedRents());
    }

    @DeleteMapping(path = "/{bookRentalId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<Void> deleteBookRental(@PathVariable Long bookRentalId) {
        bookRentalService.delete(bookRentalId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
