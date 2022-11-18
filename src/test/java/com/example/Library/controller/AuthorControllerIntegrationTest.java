package com.example.Library.controller;

import com.example.Library.model.dto.AuthorDto;
import com.example.Library.model.entity.Author;
import com.example.Library.repository.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
public class AuthorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    AuthorRepository authorRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void getAuthor_returnHttpStatusOk() throws Exception {
        Author author = createAuthor("Marko", "Adamovic");
        mockMvc.perform(get("/author" + "/{authorId}", author.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(author.getId()))
                .andExpect(jsonPath("$.firstName").value(author.getFirstName()));
    }

    @Test
    void getAuthor_returnHttpStatusNotFound_ifAuthorIsNotFound() throws Exception {
        mockMvc.perform(get("/author" + "/{authorId}", 100L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void createAuthor_returnHttpStatusCreated() throws Exception {
        AuthorDto authorDto = createAuthorDto("Marko", "Adamovic");
        String authorDtoJson = mapper.writeValueAsString(authorDto);

        mockMvc.perform(post("/author")
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
        Author author = createAuthor("Marko", "Adamovic");
        mockMvc.perform(delete("/author" + "/{authorId}", author.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAuthor_returnHttpStatusNotFound_ifAuthorIsNotFound() throws Exception {
        mockMvc.perform(delete("/author" + "/{authorId}", 100L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAuthor_returnHttpStatusOk() throws Exception {
        Author author = createAuthor("Marko", "Markovic");
        AuthorDto authorDto = createAuthorDto("Niko", "Nikolic");
        String authorDtoJson = mapper.writeValueAsString(authorDto);

        mockMvc.perform(put("/author" + "/{authorId}", author.getId())
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
        AuthorDto authorDto = createAuthorDto("Niko", "Nikolic");
        String authorDtoJson = mapper.writeValueAsString(authorDto);

        mockMvc.perform(put("/author" + "/{authorId}", 100l )
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
