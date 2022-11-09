package com.example.Library.model.mapper;

import com.example.Library.model.entity.Author;
import com.example.Library.model.dto.AuthorDto;

public class AuthorMapper {

    public static Author toEntity(AuthorDto authorDto) {
        Author author = new Author(authorDto.getFirstName(), authorDto.getLastName());
        return author;
    }

    public static AuthorDto toDto(Author author) {
        AuthorDto authorDto = new AuthorDto(author.getId(), author.getFirstName(), author.getLastName());
        return authorDto;
    }

}
