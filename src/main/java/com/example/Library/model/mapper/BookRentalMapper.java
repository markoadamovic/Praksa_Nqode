package com.example.Library.model.mapper;

import com.example.Library.model.dto.BookDto;
import com.example.Library.model.dto.BookRentalDto;
import com.example.Library.model.entity.Book;
import com.example.Library.model.entity.BookRental;

public class BookRentalMapper {

    public static BookRentalDto toDto(BookRental bookRental) {
        return new BookRentalDto(bookRental.getId(), bookRental.isRented(),
                                bookRental.getUser().getId(), bookRental.getBookCopy().getId());
    }

}
