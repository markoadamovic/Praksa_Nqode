package com.example.Library.controller;

import com.example.Library.model.dto.BookDto;
import com.example.Library.model.entity.Author;
import com.example.Library.model.entity.Book;
import com.example.Library.model.entity.User;
import com.example.Library.model.entity.UserRole;
import com.example.Library.repository.AuthorRepository;
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
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static com.example.Library.utils.TestUtils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("local")
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private Book book;

    private Author author;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    private void setUp() {
        User authUser = createUser();
        loginUser(authUser);

        author = createAuthor();
        book = createBook(TITLE, DESCRIPTION, author);
    }

    private static void loginUser(User authUser) {
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
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createBook_returnHttpStatusCreated() throws Exception {
        BookDto bookDto = createBookDto(BOOKDTO_ID, TITLE, DESCRIPTION, author.getId());
        String bookDtoJson = mapper.writeValueAsString(bookDto);

        mockMvc.perform(post(URL_BOOK_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$.description").value(bookDto.getDescription()));
    }

    @Test
    void createBook_returnHttpStatusForbidden() throws Exception {
        User regularUser = createUser(UserRole.USER);
        loginUser(regularUser);
        BookDto bookDto = createBookDto(BOOKDTO_ID, TITLE, DESCRIPTION, author.getId());
        String bookDtoJson = mapper.writeValueAsString(bookDto);

        mockMvc.perform(post(URL_BOOK_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void createBook_returnNotFoundException_ifAuthorIsNotFound() throws Exception {
        BookDto bookDto = createBookDto(BOOKDTO_ID, TITLE, DESCRIPTION, 1000L);
        String bookDtoJson = mapper.writeValueAsString(bookDto);

        mockMvc.perform(post(URL_BOOK_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getBooks_returnHttpStatusOk() throws Exception {
        mockMvc.perform(get(URL_BOOK_PREFIX))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getBook_returnHttpStatusOk() throws Exception {
        mockMvc.perform(get(URL_BOOK_PREFIX + "/{bookId}", book.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.description").value(book.getDescription()));
    }

    @Test
    void deleteBook_returnHttpStatusNoContent() throws Exception {
        mockMvc.perform(delete(URL_BOOK_PREFIX + "/{bookId}", book.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBook_returnHttpStatusIsForbidden() throws Exception {
        User regularUser = createUser(UserRole.USER);
        loginUser(regularUser);
        mockMvc.perform(delete(URL_BOOK_PREFIX + "/{bookId}", book.getId()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void updateBook_returnHttpStatusOk() throws Exception {
        BookDto bookDto = createBookDto(BOOKDTO_ID, UPDATE_BOOK_TITLE, UPDATE_DESCRIPTION, author.getId());
        String bookDtoJson = mapper.writeValueAsString(bookDto);
        mockMvc.perform(put(URL_BOOK_PREFIX + "/{bookId}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$.description").value(bookDto.getDescription()));
    }

    @Test
    void updateBook_returnHttpStatusForbidden() throws Exception {
        User regularUser = createUser(UserRole.USER);
        loginUser(regularUser);
        BookDto bookDto = createBookDto(BOOKDTO_ID, UPDATE_BOOK_TITLE, UPDATE_DESCRIPTION, author.getId());
        String bookDtoJson = mapper.writeValueAsString(bookDto);
        mockMvc.perform(put(URL_BOOK_PREFIX + "/{bookId}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void updateBook_returnHttpStatusNotFound_ifBookIsNotFound() throws Exception {
        BookDto bookDto = createBookDto(BOOKDTO_ID, TITLE, DESCRIPTION, author.getId());
        String bookDtoJson = mapper.writeValueAsString(bookDto);
        mockMvc.perform(put(URL_BOOK_PREFIX + "/{bookId}", 200l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
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

    private Author createAuthor() {
        return createAuthor(FIRSTNAME, LASTNAME);
    }

    private BookDto createBookDto(Long id, String title, String description, Long authorId) {
        BookDto bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setDescription(description);
        bookDto.setTitle(title);
        bookDto.setAuthor(authorId);

        return bookDto;
    }

    private Book createBook(String title, String description, Author author) {
        Book book = new Book();
        book.setTitle(title);
        book.setDescription(description);
        book.setAuthor(author);

        return bookRepository.save(book);
    }

    private User createUser() {
        return createUser(FIRSTNAME_USER, LASTNAME_USER,
                "adam95@gmail.com", ADDRESS, PASSWORD, USERROLE_ADMIN);
    }

    private User createUser(UserRole role) {
        return createUser(FIRSTNAME_USER, LASTNAME_USER,
                "adam995@gmail.com", ADDRESS, PASSWORD, role);
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
