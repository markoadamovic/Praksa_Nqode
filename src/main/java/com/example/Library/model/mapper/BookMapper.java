package com.example.Library.model.mapper;

import com.example.Library.model.entity.Book;
import com.example.Library.model.dto.BookDto;

public class BookMapper {

    public static Book toEntity(BookDto bookDto) {
        return new Book(bookDto.getTitle(), bookDto.getDescription());
    }

    public static BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(), book.getDescription(), book.getAuthor().getId());
    }

}
