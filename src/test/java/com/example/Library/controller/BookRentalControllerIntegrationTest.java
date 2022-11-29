package com.example.Library.controller;

import com.example.Library.model.dto.BookRentalDto;
import com.example.Library.model.entity.*;
import com.example.Library.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.example.Library.utils.TestUtils.*;
import static com.example.Library.utils.TestUtils.USERROLE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("local")
public class BookRentalControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookCopyRepository bookCopyRepository;

    @Autowired
    BookRentalRepository bookRentalRepository;

    @Autowired
    AuthorRepository authorRepository;

    private BookRental bookRental;

    private BookCopy bookCopy;

    private User user;

    private Book book;

    private Author author;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    private void setUp() {
        user = createUser(FIRSTNAME_USER, LASTNAME_USER, EMAIL, ADDRESS, PASSWORD, USERROLE);
        author = createAuthor(FIRSTNAME, LASTNAME);
        book = createBook(TITLE, DESCRIPTION, author);
        bookCopy = createBookCopy(book, IDENTIFICATION, IS_RENTED);
        bookRental = createBookRental(bookCopy, user);

        User authUser = createUser(FIRSTNAME_USER, LASTNAME_USER,
                "adam95@gmail.com", ADDRESS, PASSWORD, UserRole.ADMINISTRATOR);

        List<SimpleGrantedAuthority> grantedAuthorities = Collections
                .singletonList(new SimpleGrantedAuthority(authUser.getUserType().getName()));
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                authUser.getEmail(), authUser.getPassword(), grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    private void clean() {
        bookRentalRepository.deleteAll();
        bookCopyRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createBookRental_returnHttpStatusCreated() throws Exception {
        mockMvc.perform(post(URL_BOOKRENTAL_PREFIX + CREATE_BOOKRENTAL_URL, book.getId(), user.getId()))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void createBookRental_throwNotFoundException_ifUserIsNotFound() throws Exception {
        mockMvc.perform(post(URL_BOOKRENTAL_PREFIX + CREATE_BOOKRENTAL_URL, book.getId(), 11111L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createBookRental_throwNotFoundException_ifBookIsNotFound() throws Exception {
        mockMvc.perform(post(URL_BOOKRENTAL_PREFIX + CREATE_BOOKRENTAL_URL, 11111L, user.getId()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void endBookRental_returnHttpStatusOk() throws Exception {
        mockMvc.perform(post(URL_BOOKRENTAL_PREFIX + END_BOOKRENTAL_URL, book.getId(), bookCopy.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void endBookRental_throwNotFoundException_ifBookIsNotFound() throws Exception {
        mockMvc.perform(post(URL_BOOKRENTAL_PREFIX + END_BOOKRENTAL_URL, 1111L, bookCopy.getId()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getRentedBook_returnHttpStatusOk() throws Exception {
        mockMvc.perform(get(URL_BOOKRENTAL_PREFIX + "/{bookRentalId}", bookRental.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getRentedBook_throwNotFoundException_ifRentedBookIsNotFound() throws Exception {
        mockMvc.perform(get(URL_BOOKRENTAL_PREFIX + "/{bookRentalId}", 11111L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateRentedBook_returnHttpStatusNoContent() throws Exception{
        BookRentalDto bookRentalDto = createBookRentalDto(1L, user.getId(), bookCopy.getId());
        String bookRentalDtoJson = mapper.writeValueAsString(bookRentalDto);
        mockMvc.perform(put(URL_BOOKRENTAL_PREFIX + "/{bookRentalId}", bookRental.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookRentalDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void updateRentedBook_throwNotFoundException_ifBookRentalIsNotFound() throws Exception {
        BookRentalDto bookRentalDto = createBookRentalDto(1L, user.getId(), bookCopy.getId());
        String bookRentalDtoJson = mapper.writeValueAsString(bookRentalDto);
        mockMvc.perform(put(URL_BOOKRENTAL_PREFIX + "/{bookRentalId}", 11111L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookRentalDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateRentedBook_throwNotFoundException_ifUserIsNotFound() throws Exception {
        BookRentalDto bookRentalDto = createBookRentalDto(1L, 11111L, bookCopy.getId());
        String bookRentalDtoJson = mapper.writeValueAsString(bookRentalDto);
        mockMvc.perform(put(URL_BOOKRENTAL_PREFIX + "/{bookRentalId}", 11111L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookRentalDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getActiveRents_returnHttpStatusOk() throws Exception {
        mockMvc.perform(get(URL_BOOKRENTAL_PREFIX + ACTIVE_RENTS))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getClosedRents_returnHttpStatusOk() throws Exception {
        mockMvc.perform(get(URL_BOOKRENTAL_PREFIX + CLOSED_RENTS))
                .andDo(print())
                .andExpect(status().isOk());
    }

    public BookRental createBookRental(BookCopy bookCopy, User user) {
        BookRental bookRental = new BookRental();
        bookRental.setBookCopy(bookCopy);
        bookRental.setUser(user);

        return bookRentalRepository.save(bookRental);
    }

    private Author createAuthor(String firstName, String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);

        return authorRepository.save(author);
    }

    private User createUser(String firstName, String lastName, String email,
                            String address, String password, UserRole userRole) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAdress(address);
        user.setPassword(password);
        user.setUserType(userRole);

        return userRepository.save(user);
    }

    private Book createBook(String title, String description, Author author) {
        Book book = new Book();
        book.setTitle(title);
        book.setDescription(description);
        book.setAuthor(author);

        return bookRepository.save(book);
    }

    private BookCopy createBookCopy(Book book, String identification, boolean isRented) {
        BookCopy bookCopy = new BookCopy();
        bookCopy.setBook(book);
        bookCopy.setIdentification(identification);
        bookCopy.setRented(isRented);

        return bookCopyRepository.save(bookCopy);
    }

    private BookRentalDto createBookRentalDto(Long id, Long userId, Long bookCopyId) {
        BookRentalDto bookRentalDto = new BookRentalDto();
        bookRentalDto.setId(id);
        bookRentalDto.setUserId(userId);
        bookRentalDto.setBookCopy(bookCopyId);

        return bookRentalDto;
    }

}
