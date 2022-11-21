package com.example.Library.service;

import com.example.Library.model.dto.BookDto;
import com.example.Library.model.entity.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {

    BookDto createBook(BookDto bookDto, Long writerId);

    List<BookDto> getBooks();

    BookDto getBook(Long bookId);

    BookDto updateBook(BookDto bookDto, Long bookId);

    void delete(Long bookId);

    Book findBookModel(Long bookId);

}
