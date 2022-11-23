package com.example.Library.service;

import com.example.Library.exception.NotFoundException;
import com.example.Library.model.dto.BookRentalDto;
import com.example.Library.model.entity.*;
import com.example.Library.repository.BookRentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.Library.utils.TestUtils.*;
import static com.example.Library.utils.TestUtils.USERROLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class BookRentalServiceTest {

    @InjectMocks
    BookRentalServiceImpl bookRentalService;

    @Mock
    BookRentalRepository bookRentalRepository;

    @Mock
    BookCopyService bookCopyService;

    @Mock
    UserService userService;

    private User user;

    private BookCopy bookCopy;

    private BookRental bookRental;

    private List<BookRental> bookRentalList;

    private BookRentalDto bookRentalDto;

    private List<BookRentalDto> bookRentalDtoList;

    private Book book;

    private Author author;

    @BeforeEach
    void setup() {
        user = createUser(1l, FIRSTNAME_USER, LASTNAME_USER, ADDRESS, EMAIL,
                PASSWORD, USERROLE);
        author = createAuthor(1l,FIRSTNAME_AUTHOR, LASTNAME_AUTHOR);
        book = createBook(1l,TITLE, DESCRIPTION, author);
        bookCopy = createBookCopy(1L, IDENTIFICATION, book, IS_RENTED);
        bookRental = createBookRental(1L, bookCopy, user, DATE_START, DATE_END);
        bookRentalDto = createBookRentalDto(bookRental);
        bookRentalList = createBookRentalList(bookRental);
        bookRentalDtoList = createBookRentalDtoList(bookRentalDto);
    }

    @Test
    void createBookRentalDto_returnBookRentalDto() {
        Mockito.when(bookCopyService.findNotRentedBookCopy(any())).thenReturn(bookCopy);
        Mockito.when(userService.findUserModel(any())).thenReturn(user);
        Mockito.when(bookRentalRepository.save(any())).thenReturn(bookRental);

        BookRentalDto expected = bookRentalService.createBookRental(book.getId(), user.getId());
        assertEquals(bookRentalDto.getBookId(), expected.getBookId());
        assertEquals(bookRentalDto.getBookCopy(), expected.getBookCopy());
        assertEquals(bookRentalDto.getUserId(), expected.getUserId());
        assertEquals(bookRentalDto.getRentEnd(), expected.getRentEnd());
    }

    @Test
    void createBookRental_throwNotFoundException_ifBookCopyIsNotFound() {
        Mockito.when(bookCopyService.findNotRentedBookCopy(any())).thenThrow(new NotFoundException("all books are rented"));

        Exception exception = assertThrows(NotFoundException.class,
                () -> bookRentalService.createBookRental(book.getId(), user.getId()));
        assertTrue(exception.getMessage().contains("are rented"));
    }

    @Test
    void createBookRental_throwNotFoundException_ifUserIsNotFound() {
        Mockito.when(bookCopyService.findNotRentedBookCopy(any())).thenReturn(bookCopy);
        Mockito.when(userService.findUserModel(any())).thenThrow(new NotFoundException("user is not found"));

        Exception exception = assertThrows(NotFoundException.class,
                () -> bookRentalService.createBookRental(book.getId(), user.getId()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void createBookRental_throwNotFoundException_ifBookRentalIsNotSaved() {
        Mockito.when(bookCopyService.findNotRentedBookCopy(any())).thenReturn(bookCopy);
        Mockito.when(userService.findUserModel(any())).thenReturn(user);
        Mockito.when(bookRentalRepository.save(any())).thenThrow(new NotFoundException("not found"));

        Exception exception = assertThrows(NotFoundException.class,
                ()-> bookRentalService.createBookRental(book.getId(), user.getId()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getRentedBooks_returnListBookRentalDto() {
        Mockito.when(bookRentalRepository.findAll()).thenReturn(bookRentalList);
        List<BookRentalDto> expected = bookRentalService.getRentedBooks();

        assertEquals(bookRentalList.get(0).getBookCopy().getId(), expected.get(0).getBookCopy());
        assertEquals(bookRentalList.get(0).getUser().getId(), expected.get(0).getUserId());
        assertEquals(bookRentalList.get(0).getRentStart(), expected.get(0).getRentStart());
    }

    @Test
    void getRentedBooks_throwNotFoundException_ifBookRentalIsNotFound() {
        Mockito.when(bookRentalRepository.findAll()).thenThrow(new NotFoundException("not found"));

        Exception exception = assertThrows(NotFoundException.class, () -> bookRentalService.getRentedBooks());
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void endBookRental_returnBookRentalDto() {
        Mockito.when(bookCopyService.findBookCopyByBookId(any(), any())).thenReturn(bookCopy);
        Mockito.when(bookRentalRepository.findByBookCopy(any())).thenReturn(Optional.ofNullable(bookRental));
        Mockito.when(bookRentalRepository.save(any())).thenReturn(bookRental);

        BookRentalDto expected = bookRentalService.endBookRental(book.getId(), bookCopy.getId());
        assertEquals(bookRental.getRentStart(), expected.getRentStart());
        assertEquals(bookRental.getUser().getId(), expected.getUserId());
    }

    @Test
    void endBookRental_throwNotFoundException_ifBookCopyIsNotFound() {
        Mockito.when(bookCopyService.findBookCopyByBookId(any(), any())).thenThrow(new NotFoundException("not found"));

        Exception exception = assertThrows(NotFoundException.class,
                () -> bookRentalService.endBookRental(any(), any()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void endBookRental_throwNotFoundException_ifBookRentalIsNotFound() {
        Mockito.when(bookCopyService.findBookCopyByBookId(any(), any())).thenReturn(bookCopy);
        Mockito.when(bookRentalRepository.findByBookCopy(any())).thenThrow(new NotFoundException("not found"));

        Exception exception = assertThrows(NotFoundException.class, () -> bookRentalService.endBookRental(any(), any()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void endBookRental_throwNotFoundException_ifBookRentalIsNotSaved() {
        Mockito.when(bookCopyService.findBookCopyByBookId(any(), any())).thenReturn(bookCopy);
        Mockito.when(bookRentalRepository.findByBookCopy(any())).thenReturn(Optional.ofNullable(bookRental));
        Mockito.when(bookRentalRepository.save(any())).thenThrow(new NotFoundException("not found"));

        Exception exception = assertThrows(NotFoundException.class, () -> bookRentalService.endBookRental(any(), any()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getBookRentalByBookCopy_returnBookRental() {
        Mockito.when(bookRentalRepository.findByBookCopy(bookCopy)).thenReturn(Optional.ofNullable(bookRental));

        BookRental expected = bookRentalService.getBookRentalByBookCopy(bookCopy);
        assertEquals(bookRental.getUser(), expected.getUser());
        assertEquals(bookRental.getBookCopy(), expected.getBookCopy());
    }

    @Test
    void getBookRentalByBookCopy_throwNotFoundException_ifBookRentalIsNotFound() {
        Mockito.when(bookRentalRepository.findByBookCopy(bookCopy)).thenThrow(new NotFoundException("not found"));

        Exception exception = assertThrows(NotFoundException.class,
                () -> bookRentalService.getBookRentalByBookCopy(bookCopy));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getRentedBook_returnBookRentalDto() {
        Mockito.when(bookRentalRepository.findById(any())).thenReturn(Optional.ofNullable(bookRental));

        BookRentalDto expected = bookRentalService.getRentedBook(bookRental.getId());
        assertEquals(bookRental.getBookCopy().getId(), expected.getBookCopy());
        assertEquals(bookRental.getUser().getId(), expected.getUserId());
    }

    @Test
    void getRentedBook_throwNotFoundException_ifBookRentalIsNotFound() {
//        Mockito.when(bookRentalRepository.findById(any())).thenReturn(Optional.empty());
        Mockito.when(bookRentalRepository.findById(any())).thenThrow(new NotFoundException("not found"));

        Exception exception = assertThrows(NotFoundException.class, () -> bookRentalService.getRentedBook(any()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void updateRentedBook_returnBookRentalDto() {
        Mockito.when(bookRentalRepository.findById(any())).thenReturn(Optional.ofNullable(bookRental));
        Mockito.when(userService.findUserModel(any())).thenReturn(user);
        Mockito.when(bookRentalRepository.save(any())).thenReturn(bookRental);

        BookRentalDto expected = bookRentalService.updateRentedBook(bookRental.getId(), bookRentalDto);
        assertEquals(bookRental.getBookCopy().getId(), expected.getBookCopy());
        assertEquals(bookRental.getUser().getId(), expected.getUserId());
    }

    @Test
    void updateRentedBook_throwNotFoundException_ifBookRentalIsNotFound() {
        Mockito.when(bookRentalRepository.findById(any())).thenThrow(new NotFoundException("not found"));

        Exception exception = assertThrows(NotFoundException.class,
                () -> bookRentalService.updateRentedBook(bookRental.getId(), bookRentalDto));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void updateRentedBook_throwNotFoundException_ifUserIsNotFound() {
        Mockito.when(bookRentalRepository.findById(any())).thenReturn(Optional.ofNullable(bookRental));
        Mockito.when(userService.findUserModel(any())).thenThrow(new NotFoundException("not found"));

        Exception exception = assertThrows(NotFoundException.class,
                () -> bookRentalService.updateRentedBook(bookRental.getId(), bookRentalDto));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void updateRentedBook_throwNotFoundException_ifBookRentalIsNotSaved() {
        Mockito.when(bookRentalRepository.findById(any())).thenReturn(Optional.ofNullable(bookRental));
        Mockito.when(userService.findUserModel(any())).thenReturn(user);
        Mockito.when(bookRentalRepository.save(any())).thenThrow(new NotFoundException("not found"));

        Exception exception = assertThrows(NotFoundException.class,
                () -> bookRentalService.updateRentedBook(book.getId(), bookRentalDto));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getActiveRents_returnListBookRentalDto() {
        Mockito.when(bookRentalRepository.findByRentEnd()).thenReturn(bookRentalList);

        List<BookRentalDto> expected = bookRentalService.getActiveRents();
        assertEquals(bookRentalList.get(0).getUser().getId(), expected.get(0).getUserId());
        assertEquals(bookRentalList.get(0).getBookCopy().getId(), expected.get(0).getBookCopy());
        assertEquals(bookRentalList.get(0).getRentStart(), expected.get(0).getRentStart());
    }

    @Test
    void getClosedRents_returnListBookRentalDto() {
        Mockito.when(bookRentalRepository.findByRentStart()).thenReturn(bookRentalList);

        List<BookRentalDto> expected = bookRentalService.getClosedRents();
        assertEquals(bookRentalList.get(0).getRentStart(), expected.get(0).getRentStart());
        assertEquals(bookRentalList.get(0).getRentEnd(), expected.get(0).getRentEnd());
        assertEquals(bookRentalList.get(0).getUser().getId(), expected.get(0).getUserId());
    }

    public BookRentalDto createBookRentalDto(BookRental bookRental) {
        BookRentalDto bookRentalDto = new BookRentalDto();
        bookRentalDto.setId(bookRental.getId());
        bookRentalDto.setBookCopy(bookRental.getBookCopy().getId());
        bookRentalDto.setBookId(bookRental.getBookCopy().getBook().getId());
        bookRentalDto.setRentEnd(bookRentalDto.getRentEnd());
        bookRentalDto.setRentStart(bookRental.getRentStart());
        bookRentalDto.setUserId(bookRental.getId());

        return bookRentalDto;
    }

    private Author createAuthor(Long id, String firstName, String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setId(id);

        return author;
    }

    public List<BookRentalDto> createBookRentalDtoList(BookRentalDto bookRentalDto) {
        return List.of(bookRentalDto);
    }

    public List<BookRental> createBookRentalList(BookRental bookRental) {
        return List.of(bookRental);
    }

    private Book createBook(Long id, String title, String description, Author author) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setDescription(description);
        book.setAuthor(author);

        return book;
    }

    public BookRental createBookRental(Long id, BookCopy bookCopy, User user, LocalDate rentStart, LocalDate rentEnd) {
        BookRental bookRental = new BookRental();
        bookRental.setId(id);
        bookRental.setBookCopy(bookCopy);
        bookRental.setUser(user);
        bookRental.setRentStart(rentStart);
        bookRental.setRentEnd(rentEnd);

        return bookRental;
    }

    private BookCopy createBookCopy(Long id, String identification, Book book, boolean isRented) {
        BookCopy bookCopy = new BookCopy();
        bookCopy.setId(id);
        bookCopy.setIdentification(identification);
        bookCopy.setBook(book);
        bookCopy.setRented(isRented);

        return bookCopy;
    }

    private User createUser(Long id, String firstName, String lastName, String address,
                            String email, String password, UserRole userRole) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAdress(address);
        user.setPassword(password);
        user.setUserType(userRole);

        return user;
    }

}
