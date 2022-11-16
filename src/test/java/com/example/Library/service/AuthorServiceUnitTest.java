package com.example.Library.service;

import com.example.Library.model.dto.AuthorDto;
import com.example.Library.model.entity.Author;
import com.example.Library.model.mapper.AuthorMapper;
import com.example.Library.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceUnitTest {


    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private AuthorRepository authorRepository;

    private AuthorDto authorDto1;
    private AuthorDto authorDto2;

    private Author author1;
    private Author author2;

    private List<Author> authorsList;
    private List<AuthorDto> authorDtoList;

    @BeforeEach
    public void setup() {
        author1 = createAuthor(1l, "Marko", "Adamovic");
        author2 = createAuthor(2l, "Nikola", "Nikolic");
        authorDto1 = AuthorMapper.toDto(author1);
        authorDto2 = AuthorMapper.toDto(author2);
        authorsList = createAuthorList(author1, author2);
        authorDtoList = createAuthorDtoList(author1, author2);
    }

    private List<Author> createAuthorList(Author author1, Author author2) {
        List<Author> authors = new ArrayList<>();
        authors.add(author1);
        authors.add(author2);

        return authors;
    }

    private List<AuthorDto> createAuthorDtoList(Author author1, Author author2) {
        List<AuthorDto> authorDtos = new ArrayList<>();
        authorDtos.add(AuthorMapper.toDto(author1));
        authorDtos.add(AuthorMapper.toDto(author2));

        return authorDtos;
    }

    private Author createAuthor(Long id, String firstName, String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setId(id);

        return author;
    }

    @Test
    void shouldCreateNewAuthor() {
        Mockito.when(authorRepository.save(any())).thenReturn(author1);
        AuthorDto expectedDto = authorService.createAuthor(authorDto1);
        assertEquals(authorDto1.getId(), expectedDto.getId());
        assertEquals(authorDto1.getFirstName(), expectedDto.getFirstName());
    }

    @Test
    void findAuthor_ifIdExists_returnAuthor() {
        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.ofNullable(author1));
        Author expected = authorService.findAuthorModel(author1.getId());
        assertEquals(author1, expected);
    }

    @Test
    void getAuthor_ifIdExists_returnAuthorDto() {
        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.ofNullable(author1));
        AuthorDto expected = authorService.getAuthor(author1.getId());
        assertEquals(authorDto1.getId(), expected.getId());
        assertEquals(authorDto1.getFirstName(), expected.getFirstName());
    }

    @Test
    void getAuthors_ifAuthorsExists_returnAuthorDtoList() {
        Mockito.when(authorRepository.findAll()).thenReturn(authorsList);
        List<AuthorDto> expected = authorService.getAuthors();
        assertEquals(authorDtoList.get(0).getId(), expected.get(0).getId());
        assertEquals(authorDtoList.get(1).getId(), expected.get(1).getId());
        assertEquals(authorDtoList.get(0).getFirstName(), expected.get(0).getFirstName());
        assertEquals(authorDtoList.get(1).getFirstName(), expected.get(1).getFirstName());
    }

    @Test
    void updateAuthor_ifIdExists_returnUpdatedAuthorDto() {
        Mockito.when(authorRepository.findById(author1.getId())).thenReturn(Optional.ofNullable(author1));
        Mockito.when(authorRepository.save(author1)).thenReturn(author1);
        AuthorDto expected = authorService.updateAuthor(authorDto2, author1.getId());
        assertEquals(author1.getId(), expected.getId());
        assertEquals(authorDto2.getFirstName(), expected.getFirstName());
        assertEquals(authorDto2.getLastName(), expected.getLastName());
    }

    @Test
    void deleteAuthor_ifIdExists() {

    }

}
