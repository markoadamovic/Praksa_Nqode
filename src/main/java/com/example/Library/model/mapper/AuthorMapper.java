package com.example.Library.model.mapper;

import com.example.Library.model.entity.Author;
import com.example.Library.model.dto.AuthorDto;

public class AuthorMapper {

    public static Author toEntity(AuthorDto authorDto) {
        return new Author(authorDto.getFirstName(), authorDto.getLastName());
    }

    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getFirstName(), author.getLastName());
    }

}
