package com.example.Library.service;

import com.example.Library.model.Author;
import com.example.Library.model.dto.AuthorDto;
import com.example.Library.model.mapper.AuthorMapper;
import com.example.Library.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Author getAuthorModel(Long authorId) {
        if (authorRepository.findById(authorId).isEmpty()) {
            throw new RuntimeException("no author");
        }
        return authorRepository.findById(authorId).get();
    }

    @Override
    public AuthorDto getAuthor(Long authorId) {
        Optional<Author> authorOptional = authorRepository.findById(authorId);
        if (authorOptional.isEmpty()) {
            return null;
        }
        Author author = authorOptional.get();
        return AuthorMapper.toDto(author);
    }

    @Override
    public List<AuthorDto> getAuthors() {
        List<Author> authors = authorRepository.findAll();
        List<AuthorDto> authorDtos = new ArrayList<>();
        for (Author author : authors) {
            AuthorDto authorDto = AuthorMapper.toDto(author);
            authorDtos.add(authorDto);
        }
        return authorDtos;
    }

    @Override
    public AuthorDto updateAuthor(AuthorDto authorDto, Long authorId) {
        Optional<Author> authorOptional = authorRepository.findById(authorId);
        if (authorOptional.isEmpty()) {
            return null; // toDo Exception handling
        }

        Author author = authorOptional.get();
        author.setFirstName(authorDto.getFirstName());
        author.setLastName(authorDto.getLastName());

        return AuthorMapper.toDto(authorRepository.save(author));
    }

    @Override
    public void delete(Long authorId) {
        if (authorRepository.findById(authorId).isEmpty()) {
            throw new RuntimeException("No author");
        }
        if (authorHasBooks(authorId)) {
            throw new RuntimeException("has books");
        }
        Author author = authorRepository.findById(authorId).get();
        authorRepository.delete(author);

    }

    private Boolean authorHasBooks(Long authorId) {
        Author a = authorRepository.isAuthorAssignedToBook(authorId);
        if (a.getBooks().isEmpty()) {
            return false;
        }
        return true;
    }
}
