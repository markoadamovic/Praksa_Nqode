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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceUnitTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private AuthorDto authorDto;

    private Author author;

    private List<Author> authorsList;

    private List<AuthorDto> authorDtoList;


    @BeforeEach
    public void setup() {
        author = createAuthor();
        authorDto = AuthorMapper.toDto(author);
        authorsList = createAuthorList(author);
        authorDtoList = createAuthorDtoList(author);
    }

    @Test
    void createNewAuthor_returnAuthorDto() {
        Mockito.when(authorRepository.save(any())).thenReturn(author);
        AuthorDto expectedDto = authorService.createAuthor(authorDto);

        assertEquals(authorDto.getId(), expectedDto.getId());
        assertEquals(authorDto.getFirstName(), expectedDto.getFirstName());
    }

    @Test
    void findAuthor_returnAuthor() {
        Mockito.when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

        Author expected = authorService.findAuthorModel(author.getId());
        assertEquals(author, expected);
    }

    @Test
    void findAuthor_ifAuthorNotExists_throwNotFoundException() {
        Mockito.when(authorRepository.findById(any())).thenThrow(new NotFoundException("Author is not found"));

        Exception exception = assertThrows(NotFoundException.class, () -> authorService.findAuthorModel(any()));
        assertTrue(exception.getMessage().contains("Author is not found"));
    }

    @Test
    void getAuthor_returnAuthorDto() {
        Mockito.when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

        AuthorDto expected = authorService.getAuthor(author.getId());
        assertEquals(authorDto.getId(), expected.getId());
        assertEquals(authorDto.getFirstName(), expected.getFirstName());
    }

    @Test
    void getAuthor_ifAuthorNotExists_throwNotFoundExpception() {
        Mockito.when(authorRepository.findById(any())).thenThrow(new NotFoundException("Author is not found"));

        Exception exception = assertThrows(NotFoundException.class, () -> authorService.getAuthor(any()));
        assertTrue(exception.getMessage().contains("Author is not found"));
    }

    @Test
    void getAuthors_returnAuthorDtoList() {
        Mockito.when(authorRepository.findAll()).thenReturn(authorsList);

        List<AuthorDto> expected = authorService.getAuthors();
        assertEquals(authorDtoList.get(0).getId(), expected.get(0).getId());
        assertEquals(authorDtoList.get(0).getFirstName(), expected.get(0).getFirstName());
    }

    @Test
    void updateAuthor_returnUpdatedAuthorDto() {
        Mockito.when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        Mockito.when(authorRepository.save(author)).thenReturn(author);

        AuthorDto expected = authorService.updateAuthor(authorDto, author.getId());
        assertEquals(author.getId(), expected.getId());
        assertEquals(authorDto.getFirstName(), expected.getFirstName());
        assertEquals(authorDto.getLastName(), expected.getLastName());
    }

    @Test
    void updateAuthor_ifAuthorNotExists_throwNotFoundException() {
        Mockito.when(authorRepository.findById(any())).thenThrow(new NotFoundException("Author is not found"));

        Exception exception = assertThrows(NotFoundException.class,
                () -> authorService.updateAuthor(authorDto, any()));
        assertTrue(exception.getMessage().contains("Author is not found"));
    }

    @Test
    void deleteAuthor() {
        Mockito.when(authorRepository.isAuthorAssignedToBook(any())).thenReturn(false);
        Mockito.when(authorRepository.findById(author.getId())).thenReturn(Optional.ofNullable(author));

        authorService.delete(author.getId());
        verify(authorRepository).delete(author);
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
        Mockito.when(authorRepository.findById(any())).thenThrow(new NotFoundException("Author is not found"));

        Exception exception = assertThrows(NotFoundException.class, () -> authorService.delete(any()));
        assertTrue(exception.getMessage().contains("Author is not found"));
    }

    private List<Author> createAuthorList(Author author1) {

        return List.of(author1);
    }

    private List<AuthorDto> createAuthorDtoList(Author author) {

        return List.of(AuthorMapper.toDto(author));
    }

    private Author createAuthor(Long id, String firstName, String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setId(id);

        return author;
    }

    private Author createAuthor() {
        return createAuthor(1l, "Marko", "Adamovic");
    }

}
