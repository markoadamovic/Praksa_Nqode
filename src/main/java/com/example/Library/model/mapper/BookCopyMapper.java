package com.example.Library.model.mapper;

import com.example.Library.model.dto.BookCopyDto;
import com.example.Library.model.entity.BookCopy;

public class BookCopyMapper {

    public static BookCopyDto toDto(BookCopy bookCopy) {
        return new BookCopyDto(bookCopy.getId(), bookCopy.getBook().getId(),
                bookCopy.getIdentification(), bookCopy.isRented());
    }

}
