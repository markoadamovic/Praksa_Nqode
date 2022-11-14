package com.example.Library.service;

import com.example.Library.exception.NotFoundException;
import com.example.Library.model.entity.Author;
import com.example.Library.model.dto.AuthorDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthorService {
    AuthorDto createAuthor(AuthorDto authorDto);

    AuthorDto updateAuthor(AuthorDto authorDto, Long authorId);

    void delete(Long authorId);

    Author findAuthorModel(Long authorId) throws NotFoundException;

    AuthorDto getAuthor(Long authorId);

    List<AuthorDto> getAuthors();

}
