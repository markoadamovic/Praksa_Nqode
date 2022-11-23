package com.example.Library.model.mapper;

import com.example.Library.model.dto.BookRentalDto;
import com.example.Library.model.entity.BookRental;

public class BookRentalMapper {

    public static BookRentalDto toDto(BookRental bookRental) {
        return new BookRentalDto(bookRental.getId(), bookRental.getBookCopy().getBook().getId(),
                                bookRental.getUser().getId(), bookRental.getBookCopy().getId(),
                                bookRental.getRentStart(), bookRental.getRentEnd());
    }

}
