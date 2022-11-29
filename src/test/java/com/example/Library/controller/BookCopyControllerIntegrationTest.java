package com.example.Library.controller;

import com.example.Library.model.dto.BookCopyDto;
import com.example.Library.model.entity.*;
import com.example.Library.repository.AuthorRepository;
import com.example.Library.repository.BookCopyRepository;
import com.example.Library.repository.BookRepository;
import com.example.Library.repository.UserRepository;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static com.example.Library.utils.TestUtils.*;
import static com.example.Library.utils.TestUtils.DESCRIPTION;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("local")
public class BookCopyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BookCopyRepository bookCopyRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private Author author;

    private Book book;

    private BookCopy bookCopy;

    private User adminUser;

    @BeforeEach
    private void setUp() {
        author = createAuthor(FIRSTNAME_AUTHOR, LASTNAME_AUTHOR);
        book = createBook(TITLE, DESCRIPTION, author);
        bookCopy = createBookCopy(book, IDENTIFICATION, IS_RENTED);

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
        bookCopyRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createBookCopy_returnHttpStatusCreated() throws Exception {
        BookCopyDto bookCopyDto = createBookCopyDto(1L, book.getId(), IDENTIFICATION_UPDATE, IS_RENTED);
        String bookCopyDtoJson = mapper.writeValueAsString(bookCopyDto);

        mockMvc.perform(post(URL_BOOKCOPY_PREFIX + "/book/{bookId}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookCopyDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.identification").value(bookCopyDto.getIdentification()));
    }

    @Test
    void createBookCopy_throwNotFoundException_ifBookIsNotFound() throws Exception {
        mockMvc.perform(post(URL_BOOKCOPY_PREFIX + "/{bookId}",1000L +
                "/{aaa234}", IDENTIFICATION))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getBookCopy_returnHttpStatusOk() throws Exception {
        mockMvc.perform(get(URL_BOOKCOPY_PREFIX + "/{id}", bookCopy.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getBookCopy_throwNotFoundException_ifBookCopyIsNotFound() throws Exception {
        mockMvc.perform(get(URL_BOOKCOPY_PREFIX + "/{id}", 12345L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getBookCopies_returnHttpStatusOk() throws Exception {
        mockMvc.perform(get(URL_BOOKCOPY_PREFIX))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteBookCopy_returnHttpStatusNoContent() throws Exception {
        mockMvc.perform(delete(URL_BOOKCOPY_PREFIX + "/{id}", bookCopy.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBookCopy_ThrowNotFoundException_ifBookCopyIsNotFound() throws Exception {
        mockMvc.perform(delete(URL_BOOKCOPY_PREFIX + "/{id}", 12345L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBookCopy_returnHttpStatusOk() throws Exception {
        BookCopyDto bookCopyDto = createBookCopyDto(1L, book.getId(), IDENTIFICATION_UPDATE, IS_RENTED);
        String bookCopyDtoJson = mapper.writeValueAsString(bookCopyDto);
        mockMvc.perform(put(URL_BOOKCOPY_PREFIX + "/{id}", bookCopy.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookCopyDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identification").value(bookCopyDto.getIdentification()))
                .andExpect(jsonPath("$.bookId").value(bookCopyDto.getBookId()));
    }

    @Test
    void updateBookCopy_throwNotFoundException_ifBookCopyIsNotFound() throws Exception {
        BookCopyDto bookCopyDto = createBookCopyDto(1L, book.getId(), IDENTIFICATION_UPDATE, IS_RENTED);
        String bookCopyDtoJson = mapper.writeValueAsString(bookCopyDto);
        mockMvc.perform(put(URL_BOOKCOPY_PREFIX + "/{id}", 12345L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookCopyDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private Author createAuthor(String firstName, String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);

        return authorRepository.save(author);
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

    private BookCopyDto createBookCopyDto(Long id, Long bookId, String identification, boolean isRented) {
        BookCopyDto bookCopyDto = new BookCopyDto();
        bookCopyDto.setId(id);
        bookCopyDto.setBookId(bookId);
        bookCopyDto.setIdentification(identification);
        bookCopyDto.setRented(isRented);

        return bookCopyDto;
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

}
