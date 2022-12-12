package com.example.Library.controller;

import com.example.Library.model.dto.AuthorDto;
import com.example.Library.model.dto.BookDto;
import com.example.Library.service.BookService;
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
@RequestMapping(value = "/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(
            description = "Create Book and save it to database",
            operationId = "createBook",
            summary = "Create book",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully created",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookDto.class))
                    )
            }
    )
    @PostMapping()
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookDto));
    }

    @Operation(
            description = "Get list of all books",
            operationId = "getBooks",
            summary = "Get books",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully returned list of books",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BookDto.class)))
                    )
            }
    )
    @GetMapping
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<List<BookDto>> getBooks() {
        List<BookDto> bookList = bookService.getBooks();

        return ResponseEntity.status(HttpStatus.OK).body(bookList);
    }

    @Operation(
            description = "Get book by provided ID",
            operationId = "getBook",
            summary = "Get book",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully returned",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Book is not found",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping(path = "/{bookId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR', 'USER'})")
    public ResponseEntity<BookDto> getBook(@PathVariable Long bookId) {
        BookDto bookDto = bookService.getBook(bookId);

        return ResponseEntity.status(HttpStatus.OK).body(bookDto);
    }

    @Operation(
            description = "Delete book with provided ID",
            operationId = "deleteBook",
            summary = "Delete book",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Book is not found"),
                    @ApiResponse(responseCode = "400", description = "Book can not be deleted because" +
                            " it is assigned to another entity")
            }
    )
    @DeleteMapping(path = "/{bookId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.delete(bookId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            description = "Update book by provided ID",
            operationId = "updateBook",
            summary = "Update book",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book is updated",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Book is not found",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PutMapping(path = "/{bookId}")
    @PreAuthorize("@authService.hasAccess({'ADMINISTRATOR'})")
    public ResponseEntity<BookDto> updateBook(@RequestBody BookDto bookDto,
                                              @PathVariable Long bookId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.updateBook(bookDto, bookId));
    }

}
