package com.example.Library.controller;

import com.example.Library.model.dto.BookDto;
import com.example.Library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/book")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping
    public ResponseEntity<?> getBooks(){
        List<BookDto> bookList = bookService.getBooks();

        return new ResponseEntity<List<BookDto>>(bookList, HttpStatus.OK);
    }
    @GetMapping(path = "/{bookId}")
    public ResponseEntity<?> getBook(@PathVariable Long bookId){
        BookDto bookDto = bookService.getBook(bookId);

        return ResponseEntity.status(HttpStatus.OK).body(bookDto);
    }
    @PostMapping(path = "/writer/{writerId}")
    public ResponseEntity<?> createBook(@RequestBody BookDto bookDto, @PathVariable Long writerId){

        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookDto, writerId));

    }

    @DeleteMapping(path = "/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId){
        bookService.delete(bookId);

        return ResponseEntity.ok().build();
    }
}
