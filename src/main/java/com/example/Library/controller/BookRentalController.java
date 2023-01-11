package com.example.Library.controller;

import com.example.Library.model.dto.BookRentalDto;
import com.example.Library.service.BookRentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            description = "Create book rental and save it to database",
            operationId = "createBookRental",
            summary = "Create book rental",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Book rental is successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookRentalDto.class))
                    )
            }
    )
    @PostMapping(path = "/book/{bookId}/user/{userId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<BookRentalDto> createBookRental(@PathVariable Long bookId,
                                                          @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookRentalService.createBookRental(bookId, userId));
    }

    @Operation(
            description = "End book rental for the bookCopy with provided book ID",
            operationId = "endBookRental",
            summary = "End book rental",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book rental is successfully finished",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookRentalDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "BookCopy with provided ID is not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping(path = "/book/{bookId}/bookCopy/{bookCopyId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<BookRentalDto> endBookRental(@PathVariable Long bookId,
                                                       @PathVariable Long bookCopyId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.endBookRental(bookId, bookCopyId));
    }

    @Operation(
            description = "Get list of books that are already rented",
            operationId = "getRentedBooks",
            summary = "Get rented books",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of book rentals is successfully returned",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookRentalDto.class)))
                    )
            }
    )
    @GetMapping
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<List<BookRentalDto>> getBookRentals() {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.getBookRentals());
    }

    @Operation(
            description = "Get BookRental by provided ID",
            operationId = "getBookRental",
            summary = "Get book rental",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "BookRental is successfully returned",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookRentalDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "BookRental with provided ID is not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @GetMapping(path = "/{bookRentalId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<BookRentalDto> getBookRental(@PathVariable Long bookRentalId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.getBookRental(bookRentalId));
    }

    @Operation(
            description = "Update BookRental by provided ID",
            operationId = "updateBookRental",
            summary = "Update book rental",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "BookRental is successfully updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookRentalDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "BookRental with provided ID is not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PutMapping(path = "/{bookRentalId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<BookRentalDto> updateRentedBook(@PathVariable Long bookRentalId,
                                                          @RequestBody BookRentalDto bookRentalDto) {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.updateRentedBook(bookRentalId, bookRentalDto));
    }

    @Operation(
            description = "Get list of BookRentals with active rent status",
            operationId = "getActiveRents",
            summary = "Get active book rentals",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of BookRentals is successfully returned",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookRentalDto.class)))
                    )
            }
    )
    @GetMapping(value = "/activeRents")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<List<BookRentalDto>> getActiveRents() {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.getActiveRents());
    }

    @Operation(
            description = "Get list of BookRentals with closed rent status",
            operationId = "getClosedRents",
            summary = "Get closed book rentals",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of BookRentals is successfully returned",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookRentalDto.class)))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "BookRental with provided ID is not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @GetMapping(value = "/closedRents")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<List<BookRentalDto>> getClosedRents() {
        return ResponseEntity.status(HttpStatus.OK).body(bookRentalService.getClosedRents());
    }

    @Operation(
            description = "Delete book rental with provided ID",
            operationId = "deleteBookRental",
            summary = "Delete book rental",
            responses = {
                    @ApiResponse(responseCode = "204", description = "BookRental is successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "BookRental with provided ID is not found")
            }
    )
    @DeleteMapping(path = "/{bookRentalId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<Void> deleteBookRental(@PathVariable Long bookRentalId) {
        bookRentalService.delete(bookRentalId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
