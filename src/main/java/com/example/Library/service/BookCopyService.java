package com.example.Library.service;

import com.example.Library.model.dto.BookCopyDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookCopyService {

    BookCopyDto createBookCopy(Long bookId, String identification);

    BookCopyDto getBookCopy(Long id);

    List<BookCopyDto> getBookCopies();

    void delete(Long id);

    BookCopyDto updateBookCopy(Long id, BookCopyDto bookCopyDto);

}
