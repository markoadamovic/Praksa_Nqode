package com.example.Library.controller;

import com.example.Library.model.dto.BookDto;
import com.example.Library.model.entity.Author;
import com.example.Library.model.entity.Book;
import com.example.Library.repository.AuthorRepository;
import com.example.Library.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("local")
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    ObjectMapper mapper = new ObjectMapper();

    private final String FIRSTNAME = "Marko";
    private final String LASTNAME = "Adamovic";
    private final String TITLE = "Lord of the rings";
    private final String DESCRIPTION = "Action";
    private final String BOOK_TITLE = "Na Drini cuprija";
    private final String BOOK_DESCRIPTION = "Drama";

    @Test
    void createBook_returnHttpStatusCreated() throws Exception {
        Author author = createAuthor(FIRSTNAME, LASTNAME);
        BookDto bookDto = createBookDto(1l,TITLE, DESCRIPTION, author.getId());
        String bookDtoJson = mapper.writeValueAsString(bookDto);

        mockMvc.perform(post("/book" + "/{authorId}", author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$.description").value(bookDto.getDescription()));
    }

    @Test
    void createBook_returnNotFoundException_ifAuthorIsNotFound() throws Exception {
        BookDto bookDto = createBookDto(1l,TITLE, DESCRIPTION, 1L);
        String bookDtoJson = mapper.writeValueAsString(bookDto);

        mockMvc.perform(post("/book" + "/{authorId}", 1000L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getBooks_returnHttpStatusOk() throws Exception {
        mockMvc.perform(get("/book"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getBook_returnHttpStatusOk() throws Exception {
        Author author = createAuthor(FIRSTNAME, LASTNAME);
        Book book = createBook(BOOK_TITLE, BOOK_DESCRIPTION, author);
        mockMvc.perform(get("/book" + "/{bookId}", book.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.description").value(book.getDescription()));
    }


    @Test
    void deleteBook_returnHttpStatusNoContent() throws Exception {
        Author author = createAuthor(FIRSTNAME, LASTNAME);
        Book book = createBook(BOOK_TITLE, BOOK_DESCRIPTION, author);
        mockMvc.perform(delete("/book" + "/{bookId}", book.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBook_returnHttpStatusNotFound_ifAuthorIsNotFound() throws Exception {
        mockMvc.perform(delete("/book" + "/{bookId}", 20L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBook_returnHttpStatusOk() throws Exception {
        Author author = createAuthor(FIRSTNAME, LASTNAME);
        Book book = createBook(BOOK_TITLE, BOOK_DESCRIPTION, author);
        BookDto bookDto = createBookDto(1l,TITLE, DESCRIPTION, author.getId());
        String bookDtoJson = mapper.writeValueAsString(bookDto);
        mockMvc.perform(put("/book" + "/{bookId}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$.description").value(bookDto.getDescription()))
                .andExpect(jsonPath("$.id").value(bookDto.getId()));
    }

    @Test
    void updateBook_returnHttpStatusNotFound_ifBookIsNotFound() throws Exception {
        Author author = createAuthor(FIRSTNAME, LASTNAME);
        BookDto bookDto = createBookDto(1l,TITLE, DESCRIPTION, author.getId());
        String bookDtoJson = mapper.writeValueAsString(bookDto);
        mockMvc.perform(put("/book" + "/{bookId}", 200l)
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
}
