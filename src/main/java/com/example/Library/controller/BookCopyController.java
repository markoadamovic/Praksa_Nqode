package com.example.Library.controller;

import com.example.Library.model.dto.AuthorDto;
import com.example.Library.model.dto.BookCopyDto;
import com.example.Library.model.dto.BookDto;
import com.example.Library.service.BookCopyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            description = "Create book copy for book with provided ID and save it to database",
            operationId = "updateBook",
            summary = "Update book",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Book is created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookCopyDto.class))
                    )
            }
    )
    @PostMapping(value = "/book/{bookId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<BookCopyDto> createBookCopy(@PathVariable Long bookId,
                                                      @RequestBody BookCopyDto bookCopyDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookCopyService.createBookCopy(bookId, bookCopyDto));
    }

    @Operation(
            description = "Get book copy by provided ID",
            operationId = "getBookCopy",
            summary = "Get book copy",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "BookCopy is successfully returned",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookCopyDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "BookCopy with provided ID is not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @GetMapping(path = "/{bookCopyId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<BookCopyDto> getBookCopy(@PathVariable Long bookCopyId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookCopyService.getBookCopy(bookCopyId));
    }

    @Operation(
            description = "Get list of book copies",
            operationId = "getBookCopies",
            summary = "Get book copies",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "BookCopy list is successfully returned",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookCopyDto.class)))
                    )
            }
    )
    @GetMapping
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<List<BookCopyDto>> getBookCopies() {
        return ResponseEntity.status(HttpStatus.OK).body(bookCopyService.getBookCopies());
    }

    @Operation(
            description = "Delete book copy with provided ID",
            operationId = "deleteBookCopy",
            summary = "Delete book copy",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Book is successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "BookCopy with provided ID is not found"),
                    @ApiResponse(responseCode = "400", description = "BookCopy can not be deleted because" +
                            " it is assigned to another entity")
            }
    )
    @DeleteMapping(path = "/{bookCopyId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<Void> deleteBookCopy(@PathVariable Long bookCopyId) {
        bookCopyService.delete(bookCopyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            description = "Update book copy by provided ID",
            operationId = "updateBookCopy",
            summary = "Update book copy",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book is successfully returned",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookCopyDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "BookCopy with provided ID is not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PutMapping(path = "/{bookCopyId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<BookCopyDto> updateBookCopy(@PathVariable Long bookCopyId,
                                                      @RequestBody BookCopyDto bookCopyDto) {
        return ResponseEntity.status(HttpStatus.OK).body(bookCopyService.updateBookCopy(bookCopyId, bookCopyDto));
    }

}
