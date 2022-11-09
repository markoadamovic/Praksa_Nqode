package com.example.Library.model.mapper;

import com.example.Library.model.Book;
import com.example.Library.model.dto.BookDto;

public class BookMapper {

    public static Book toEntity(BookDto bookDto) {
        Book book = new Book(bookDto.getTitle(), bookDto.getDescription());
        return book;
    }

    public static BookDto toDto(Book book) {
        BookDto bookDto = new BookDto(book.getId(), book.getTitle(),
                book.getDescription(), book.getAuthor().getId());
        return bookDto;
    }

}
