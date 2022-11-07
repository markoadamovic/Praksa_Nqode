package com.example.Library.service;

import com.example.Library.model.dto.BookDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {

    List<BookDto> getBooks();

    BookDto createBook(BookDto bookDto, Long writerId);



}
