package com.example.Library.service;

import com.example.Library.exception.AuthorIsAssignedToBookException;
import com.example.Library.exception.NotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

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

    @Test
    void shouldCreateNewAuthor() {
        Mockito.when(authorRepository.save(any())).thenReturn(author1);

        AuthorDto expectedDto = authorService.createAuthor(authorDto1);
        assertEquals(authorDto1.getId(), expectedDto.getId());
        assertEquals(authorDto1.getFirstName(), expectedDto.getFirstName());
    }

    @Test
    void findAuthor_ifAuthorExists_returnAuthor() {
        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.ofNullable(author1));

        Author expected = authorService.findAuthorModel(author1.getId());
        assertEquals(author1, expected);
    }

    @Test
    void findAuthor_ifAuthorNotExists_throwNotFoundException() {
        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> authorService.findAuthorModel(any()));
        assertTrue(exception.getMessage().contains("Author is not found"));
    }

    @Test
    void getAuthor_ifAuthorExists_returnAuthorDto() {
        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.ofNullable(author1));

        AuthorDto expected = authorService.getAuthor(author1.getId());
        assertEquals(authorDto1.getId(), expected.getId());
        assertEquals(authorDto1.getFirstName(), expected.getFirstName());
    }

    @Test
    void getAuthor_ifAuthorNotExists_throwNotFoundExpception() {
        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> authorService.getAuthor(any()));
        assertTrue(exception.getMessage().contains("Author is not found"));
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
    void updateAuthor_ifAuthorExists_returnUpdatedAuthorDto() {
        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.ofNullable(author1));
        Mockito.when(authorRepository.save(author1)).thenReturn(author1);

        AuthorDto expected = authorService.updateAuthor(authorDto2, any());
        assertEquals(author1.getId(), expected.getId());
        assertEquals(authorDto2.getFirstName(), expected.getFirstName());
        assertEquals(authorDto2.getLastName(), expected.getLastName());
    }

    @Test
    void updateAuthor_ifAuthorNotExists_throwNotFoundException() {
        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class,
                () -> authorService.updateAuthor(authorDto1, any()));
        assertTrue(exception.getMessage().contains("Author is not found"));
    }

    @Test
    void deleteAuthor_ifAuthorExists() {
        Mockito.when(authorRepository.isAuthorAssignedToBook(any())).thenReturn(false);
        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.ofNullable(author1));

        authorService.delete(author1.getId());
        verify(authorRepository).delete(author1);
    }

    @Test
    void deleteAuthor_ifAuthorIsAssignedToBook_throwAuthorIsAssignedToBookException() {
        Mockito.when(authorRepository.isAuthorAssignedToBook(any())).thenReturn(true);

        Exception exception = assertThrows(AuthorIsAssignedToBookException.class, () -> authorService.delete(any()));
        assertTrue(exception.getMessage().contains("has book assigned to him"));
    }

    @Test
    void deleteAuthor_ifAuthorNotExists_throwNotFoundException() {
        Mockito.when(authorRepository.isAuthorAssignedToBook(any())).thenReturn(false);
        Mockito.when(authorRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> authorService.delete(any()));
        assertTrue(exception.getMessage().contains("Author is not found"));
    }

    private List<Author> createAuthorList(Author author1, Author author2) {

        return List.of(author1, author2);
    }

    private List<AuthorDto> createAuthorDtoList(Author author1, Author author2) {

        return List.of(AuthorMapper.toDto(author1), AuthorMapper.toDto(author2));
    }

    private Author createAuthor(Long id, String firstName, String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setId(id);

        return author;
    }

}
