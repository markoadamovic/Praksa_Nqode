package com.example.Library.controller;

import com.example.Library.model.dto.AuthorDto;
import com.example.Library.model.entity.Author;
import com.example.Library.repository.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.Library.utils.TestUtils.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("local")
class AuthorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private Author author;

    @BeforeEach
    private void setUp() {
        author = createAuthor(FIRSTNAME_AUTHOR, LASTNAME_AUTHOR);
    }

    @AfterEach
    private void clean() {
        authorRepository.deleteAll();
    }

    @Test
    void getAuthor_returnHttpStatusOk() throws Exception {
        mockMvc.perform(get(URL_AUTHOR_PREFIX + "/{authorId}", author.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(author.getId()))
                .andExpect(jsonPath("$.firstName").value(author.getFirstName()));
    }

    @Test
    void getAuthor_returnHttpStatusNotFound_ifAuthorIsNotFound() throws Exception {
        mockMvc.perform(get(URL_AUTHOR_PREFIX + "/{authorId}", 100L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createAuthor_returnHttpStatusCreated() throws Exception {
        AuthorDto authorDto = createAuthorDto(FIRSTNAME_AUTHOR, LASTNAME_AUTHOR);
        String authorDtoJson = mapper.writeValueAsString(authorDto);

        mockMvc.perform(post(URL_AUTHOR_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(authorDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(authorDto.getLastName()));
    }

    @Test
    void deleteAuthor_returnHttpStatusNoContent() throws Exception {
        mockMvc.perform(delete(URL_AUTHOR_PREFIX + "/{authorId}", author.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAuthor_returnHttpStatusNotFound_ifAuthorIsNotFound() throws Exception {
        mockMvc.perform(delete(URL_AUTHOR_PREFIX + "/{authorId}", 100L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAuthor_returnHttpStatusOk() throws Exception {
        AuthorDto authorDto = createAuthorDto(FIRSTNAME_AUTHOR_DTO, LASTNAME_AUTHOR_DTO);
        String authorDtoJson = mapper.writeValueAsString(authorDto);

        mockMvc.perform(put(URL_AUTHOR_PREFIX + "/{authorId}", author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(authorDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(authorDto.getLastName()));
    }

    @Test
    void updateAuthor_returnHttpStatusNotFound_ifAuthorIsNotFound() throws Exception {
        AuthorDto authorDto = createAuthorDto(FIRSTNAME_AUTHOR_DTO, LASTNAME_AUTHOR_DTO);
        String authorDtoJson = mapper.writeValueAsString(authorDto);

        mockMvc.perform(put(URL_AUTHOR_PREFIX + "/{authorId}", 100l )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private AuthorDto createAuthorDto(String firstName, String lastName) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setFirstName(firstName);
        authorDto.setLastName(lastName);

        return authorDto;
    }

    private Author createAuthor(String firstName, String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);

        return authorRepository.save(author);
    }

}
