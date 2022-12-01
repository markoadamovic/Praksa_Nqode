package com.example.Library.service;

import com.example.Library.model.dto.BookRentalDto;
import com.example.Library.model.entity.BookCopy;
import com.example.Library.model.entity.BookRental;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookRentalService {

    BookRentalDto createBookRental(Long bookId, Long userId);

    void delete(Long bookRentalId);

    List<BookRentalDto> getRentedBooks();

    BookRentalDto endBookRental(Long bookId, Long bookCopyId);

    BookRental getBookRentalByBookCopy(BookCopy bookCopy);

    BookRentalDto getRentedBook(Long bookRentalId);

    BookRentalDto updateRentedBook(Long bookRentalId, BookRentalDto bookRentalDto);

    List<BookRentalDto> getActiveRents();

    List<BookRentalDto> getClosedRents();

}

