package com.example.Library.service;

import com.example.Library.exception.NotFoundException;
import com.example.Library.model.dto.BookDto;
import com.example.Library.model.entity.Author;
import com.example.Library.model.entity.Book;
import com.example.Library.model.mapper.BookMapper;
import com.example.Library.repository.BookRepository;
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
public class BookServiceUnitTest {

    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    AuthorService authorService;

    private Book book;

    private Book book1;

    private BookDto bookDto;

    private BookDto bookDto1;

    private Author author;

    private List<Book> bookList;

    @BeforeEach
    public void setup() {
        book = createBook(1l,"Na drini cuprija", "knjiga");
        book1 = createBook(2l, "Novi Sad", "Zanimljivo");
        author = createAuthor(1l,"Marko", "Adamovic");
        book.setAuthor(author);
        book1.setAuthor(author);
        bookDto = BookMapper.toDto(book);
        bookDto1 = BookMapper.toDto(book1);
        bookList = createBookList(book, book1);
    }

    private List<Book> createBookList(Book book, Book book1) {

        return List.of(book, book1);
    }

    private Author createAuthor(Long id, String firstName, String lastName) {
        Author author = new Author();
        author.setId(id);
        author.setFirstName(firstName);
        author.setLastName(lastName);

        return author;
    }

    private Book createBook(Long id, String title, String description) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setDescription(description);

        return book;
    }

    @Test
    void createBook_ifAuthorExists_returnBookDto(){
        Mockito.when(authorService.findAuthorModel(any())).thenReturn(author);
        Mockito.when(bookRepository.save(any())).thenReturn(book);

        BookDto expected = bookService.createBook(bookDto, author.getId());
        assertEquals(bookDto.getId(), expected.getId());
    }

    @Test
    void createBook_IfAuthorNotExists_ThrowNotFoundException() {
        Mockito.when(authorService.findAuthorModel(any())).thenThrow(new NotFoundException("Author is not found"));

        Exception exception = assertThrows(NotFoundException.class, () -> bookService.createBook(bookDto, any()));
        assertTrue(exception.getMessage().contains("Author is not found"));
    }

    @Test
    void getBooks_ifBooksExists_returnBookListDto() {
        Mockito.when(bookRepository.findAll()).thenReturn(bookList);

        List<BookDto> expected = bookService.getBooks();
        assertEquals(bookList.get(0).getId(), expected.get(0).getId());
        assertEquals(bookList.get(0).getDescription(), expected.get(0).getDescription());
    }

    @Test
    void findBookModel_ifBookExists() {
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.ofNullable(book));

        Book expected = bookService.findBookModel(book.getId());
        assertEquals(book, expected);
    }

    @Test
    void findBookModel_ifBookNotExists_thenThrowNotFoundException() {
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> bookService.findBookModel(any()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getBook_ifBookExist_returnBookDto() {
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.ofNullable(book));

        BookDto expected = bookService.getBook(book.getId());
        assertEquals(book.getId(), expected.getId());
        assertEquals(book.getDescription(), expected.getDescription());
    }

    @Test
    void getBook_ifBookNotExists_throwNotFoundException() {
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> bookService.getBook(any()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void deleteBook_ifBookExist() {
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.ofNullable(book));

        bookService.delete(book.getId());
        verify(bookRepository).delete(book);
    }

    @Test
    void deleteBook_ifBookNotExists_throwNotFoundException() {
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class, () -> bookService.delete(any()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void updateBook_ifBookAndAuthorExists_returnBookDto() {
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.ofNullable(book));
        Mockito.when(authorService.findAuthorModel(any())).thenReturn(author);
        Mockito.when(bookRepository.save(book)).thenReturn(book);

        BookDto expected = bookService.updateBook(bookDto1, book.getId());
        assertEquals(book.getId(), expected.getId());
        assertEquals(book.getDescription(), expected.getDescription());
        assertEquals(book.getAuthor().getId(), expected.getAuthorId());
    }

    @Test
    void updateBook_ifBookNotExists_throwNotFoundException() {
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> bookService.updateBook(bookDto, any()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void updateBook_ifAuthorNotExists_throwNotFoundException() {
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.ofNullable(book));
        Mockito.when(authorService.findAuthorModel(any())).thenThrow(new NotFoundException("not found"));

        Exception exception = assertThrows(NotFoundException.class,
                () -> bookService.updateBook(bookDto, book.getId()));
        assertTrue(exception.getMessage().contains("not found"));
    }

}
