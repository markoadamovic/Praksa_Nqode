package com.example.Library.service;

import com.example.Library.exception.BadRequestException;
import com.example.Library.exception.NotFoundException;
import com.example.Library.model.dto.BookCopyDto;
import com.example.Library.model.entity.Author;
import com.example.Library.model.entity.Book;
import com.example.Library.model.entity.BookCopy;
import com.example.Library.model.mapper.BookCopyMapper;
import com.example.Library.repository.BookCopyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.Library.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookCopyServiceUnitTest {

    @InjectMocks
    BookCopyServiceImpl bookCopyService;

    @Mock
    BookCopyRepository bookCopyRepository;

    @Mock
    BookService bookService;


    private BookCopy bookCopy;

    private BookCopyDto bookCopyDto;

    private Book book;

    private Author author;

    private List<BookCopy> bookCopyList;

    @BeforeEach
    void setup() {
        author = createAuthor();
        book = createBook();
        bookCopy = createBookCopy();
        bookCopyDto = BookCopyMapper.toDto(bookCopy);
        bookCopyList = List.of(bookCopy);
    }

    @Test
    void createBookCopy_returnBookCopyDto() {
        Mockito.when(bookCopyRepository.findByIdentification(any())).thenReturn(Optional.empty());
        Mockito.when(bookService.findBookModel(book.getId())).thenReturn(book);
        Mockito.when(bookCopyRepository.save(Mockito.any(BookCopy.class))).thenReturn(bookCopy);

        BookCopyDto expected = bookCopyService.createBookCopy(book.getId(), bookCopyDto);
        assertEquals(bookCopy.getIdentification(), expected.getIdentification());
    }

    @Test
    void createBookCopy_throwBadRequestException_ifIdentificatorNotUnique() {
        Mockito.when(bookCopyRepository.findByIdentification(bookCopy.getIdentification())).thenReturn(Optional.of(bookCopy));

        Exception exception = assertThrows(BadRequestException.class,
                () -> bookCopyService.createBookCopy(1l, bookCopyDto));
        assertTrue(exception.getMessage().contains("exists"));
    }

    @Test
    void createBookCopy_throwNotFoundException_ifBookIsNotFound() {
        Mockito.when(bookCopyRepository.findByIdentification(any())).thenReturn(Optional.empty());
        Mockito.when(bookService.findBookModel(any())).thenThrow(new NotFoundException("not found"));

        Exception exception = assertThrows(NotFoundException.class,
                () -> bookCopyService.createBookCopy(1L, bookCopyDto));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getBookCopy_returnBookCopyDto() {
        Mockito.when(bookCopyRepository.findById(bookCopy.getId())).thenReturn(Optional.of(bookCopy));

        BookCopyDto expected = bookCopyService.getBookCopy(bookCopy.getId());
        assertEquals(bookCopy.getId(), expected.getBookId());
        assertEquals(bookCopy.getIdentification(), expected.getIdentification());
    }

    @Test
    void getBookCopy_throwNotFoundException_ifBookCopyIsNotFound() {
        Mockito.when(bookCopyRepository.findById(any())).thenThrow(new NotFoundException("BookCopy not found"));

        Exception exception = assertThrows(NotFoundException.class, () -> bookCopyService.getBookCopy(1L));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getBookCopies_returnListBookCopyDto() {
        Mockito.when(bookCopyRepository.findAll()).thenReturn(bookCopyList);

        List<BookCopyDto> expected = bookCopyService.getBookCopies();
        assertEquals(bookCopyList.get(0).getIdentification(), expected.get(0).getIdentification());
    }

    @Test
    void deleteBookCopy() {
        Mockito.when(bookCopyRepository.findById(bookCopy.getId())).thenReturn(Optional.of(bookCopy));
        Mockito.when(bookCopyRepository.isBookCopyRented(bookCopy.getId())).thenReturn(false);

        bookCopyService.delete(bookCopy.getId());
        verify(bookCopyRepository).delete(bookCopy);
    }

    @Test
    void deleteBookCopy_throwNotFoundException_ifBookCopyNotFound() {
        Mockito.when(bookCopyRepository.findById(any())).thenThrow(new NotFoundException("BookCopy not found"));

        Exception exception = assertThrows(NotFoundException.class, () -> bookCopyService.delete(1L));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void deleteBookCopy_throwBadRequestException_ifBookIsRented() {
        Mockito.when(bookCopyRepository.findById(bookCopy.getId())).thenReturn(Optional.of(bookCopy));
        Mockito.when(bookCopyRepository.isBookCopyRented(bookCopy.getId())).thenReturn(true);

        Exception exception = assertThrows(BadRequestException.class, () -> bookCopyService.delete(bookCopy.getId()));
        assertTrue(exception.getMessage().contains("Book copy is rented"));
    }

    @Test
    void updateBookCopy_returnBookCopyDto() {
        Mockito.when(bookCopyRepository.findById(bookCopy.getId())).thenReturn(Optional.of(bookCopy));
        Mockito.when(bookService.findBookModel(book.getId())).thenReturn(book);
        Mockito.when(bookCopyRepository.save(any())).thenReturn(bookCopy);

        BookCopyDto expected = bookCopyService.updateBookCopy(1L, bookCopyDto);
        assertEquals(bookCopy.getIdentification(), expected.getIdentification());
        assertEquals(bookCopy.getBook().getId(), expected.getBookId());
    }

    @Test
    void updateBookCopy_throwNotFoundException_ifBookCopyIsNotFound() {
        Mockito.when(bookCopyRepository.findById(any())).thenThrow(new NotFoundException("BookCopy not found"));

        Exception exception = assertThrows(NotFoundException.class,
                () -> bookCopyService.updateBookCopy(1L, bookCopyDto));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void updateBookCopy_throwNotFoundException_ifBookIsNotFound() {
        Mockito.when(bookCopyRepository.findById(any())).thenReturn(Optional.of(bookCopy));
        Mockito.when(bookService.findBookModel(any())).thenThrow(new NotFoundException("not found"));

        Exception exception = assertThrows(NotFoundException.class,
                () -> bookCopyService.updateBookCopy(1L, bookCopyDto));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void bookCopyWithIdentificationNotExists() {
        Mockito.when(bookCopyRepository.findByIdentification(any())).thenReturn(Optional.empty());

        Boolean expected = bookCopyService.bookCopyWithIdentificationExists("aasd");
        assertEquals(false, expected);
    }

    @Test
    void bookCopyWithIdentificationExists() {
        Mockito.when(bookCopyRepository.findByIdentification(bookCopy.getIdentification())).thenReturn(Optional.of(bookCopy));

        Boolean expected = bookCopyService.bookCopyWithIdentificationExists(bookCopy.getIdentification());
        assertEquals(true, expected );
    }

    @Test
    void findBookCopyModel_returnBookCopy() {
        Mockito.when(bookCopyRepository.findById(bookCopy.getId())).thenReturn(Optional.of(bookCopy));

        BookCopy expected = bookCopyService.findBookCopyModel(bookCopy.getId());
        assertEquals(bookCopy.getId(), expected.getId());
        assertEquals(bookCopy.getIdentification(), expected.getIdentification());
    }

    @Test
    void findBookCopyModel_throwNotFoundException_ifBookCopyIsNotFound() {
        Mockito.when(bookCopyRepository.findById(any())).thenThrow(new NotFoundException("BookCopy not found"));

        Exception exception = assertThrows(NotFoundException.class, () -> bookCopyService.findBookCopyModel(1L));
        assertTrue(exception.getMessage().contains("not found"));
    }

    private BookCopy createBookCopy(Long id, String identification, Book book, boolean isRented) {
        BookCopy bookCopy = new BookCopy();
        bookCopy.setId(id);
        bookCopy.setIdentification(identification);
        bookCopy.setBook(book);
        bookCopy.setRented(isRented);

        return bookCopy;
    }

    private BookCopy createBookCopy() {
        return createBookCopy(1L, IDENTIFICATION, book, IS_RENTED);
    }

    private Book createBook(Long id, String title, String description, Author author) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setDescription(description);
        book.setAuthor(author);

        return book;
    }

    private Book createBook() {
        return createBook(1L, TITLE, DESCRIPTION, author);
    }

    private Author createAuthor(Long id, String firstName, String lastName) {
        Author author = new Author();
        author.setId(id);
        author.setFirstName(firstName);
        author.setLastName(lastName);

        return author;
    }

    private Author createAuthor() {
        return createAuthor(1L, FIRSTNAME_AUTHOR, LASTNAME_AUTHOR);
    }

}
