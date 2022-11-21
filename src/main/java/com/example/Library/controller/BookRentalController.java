package com.example.Library.controller;

import com.example.Library.model.dto.BookRentalDto;
import com.example.Library.service.BookRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book/{bookId}/book-copy/{bookCopyId}/user/{userId}/rent")
public class BookRentalController {

    private final BookRentalService bookRentalService;

    @PostMapping()
    public ResponseEntity<BookRentalDto> createBookRental(@PathVariable Long bookCopyId,
                                                          @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookRentalService.createBookRental(bookCopyId, userId));
    }

}
