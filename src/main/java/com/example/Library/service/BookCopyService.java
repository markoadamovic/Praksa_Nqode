package com.example.Library.service;

import com.example.Library.model.dto.BookCopyDto;
import com.example.Library.model.entity.Book;
import com.example.Library.model.entity.BookCopy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookCopyService {

    BookCopyDto createBookCopy(Long bookId, BookCopyDto bookCopyDto);

    BookCopyDto getBookCopy(Long bookCopyId);

    List<BookCopyDto> getBookCopies();

    void delete(Long bookCopyId);

    BookCopyDto updateBookCopy(Long bookCopyId, BookCopyDto bookCopyDto);

    BookCopy findBookCopyModel(Long bookCopyId);

    List<BookCopy> findBookCopiesByBook(Long bookId);

    BookCopy findBookCopyByBookId(Long bookId, Long bookCopyId);

    BookCopy findNotRentedBookCopy(Long bookId);

}
