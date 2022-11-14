package com.example.Library.service;

import com.example.Library.exception.AuthorIsAssignedToBookException;
import com.example.Library.exception.NotFoundException;
import com.example.Library.model.entity.Author;
import com.example.Library.model.dto.AuthorDto;
import com.example.Library.model.mapper.AuthorMapper;
import com.example.Library.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorDto createAuthor(AuthorDto authorDto) {
        Author author = AuthorMapper.toEntity(authorDto);

        return AuthorMapper.toDto(authorRepository.save(author));
    }

    @Override
    public Author findAuthorModel(Long authorId) throws NotFoundException {
        return authorRepository.findById(authorId).orElseThrow(() -> new NotFoundException("Author is not found"));
    }

    @Override
    public AuthorDto getAuthor(Long authorId) {
        Author author = findAuthorModel(authorId);

        return AuthorMapper.toDto(author);
    }

    @Override
    public List<AuthorDto> getAuthors() {
        List<Author> authors = authorRepository.findAll();

        return authors.stream()
                .map(AuthorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDto updateAuthor(AuthorDto authorDto, Long authorId) {
        Author author = findAuthorModel(authorId);
        author.setFirstName(authorDto.getFirstName());
        author.setLastName(authorDto.getLastName());

        return AuthorMapper.toDto(authorRepository.save(author));
    }

    @Override
    public void delete(Long authorId) {
        if (authorHasBooks(authorId)) {
            throw new AuthorIsAssignedToBookException("Author has books");
        }

        Author author = findAuthorModel(authorId);
        authorRepository.delete(author);

    }

    private Boolean authorHasBooks(Long authorId) {
        return authorRepository.isAuthorAssignedToBook(authorId);
    }

}
