package com.example.Library.controller;

import com.example.Library.model.dto.AuthorDto;
import com.example.Library.model.entity.Author;
import com.example.Library.model.entity.User;
import com.example.Library.model.entity.UserRole;
import com.example.Library.repository.AuthorRepository;
import com.example.Library.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.After;
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

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private Author author;

    @BeforeEach
    private void setUp() {
        User authUser = createUser();
        loginUser(authUser);

        author = createAuthor();
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
        userRepository.deleteAll();
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
        mockMvc.perform(get(URL_AUTHOR_PREFIX + "/{authorId}", 1234L))
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
    void deleteAuthor_returnHttpStatusForbidden() throws Exception {
        User regularUser = createUser(UserRole.USER);
        loginUser(regularUser);
        mockMvc.perform(delete(URL_AUTHOR_PREFIX + "/{authorId}", author.getId()))
                .andDo(print())
                .andExpect(status().isForbidden());
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
    void updateAuthor_returnHttpStatusForbidden() throws Exception {
        User regularUser = createUser(UserRole.USER);
        loginUser(regularUser);
        AuthorDto authorDto = createAuthorDto(FIRSTNAME_AUTHOR_DTO, LASTNAME_AUTHOR_DTO);
        String authorDtoJson = mapper.writeValueAsString(authorDto);

        mockMvc.perform(put(URL_AUTHOR_PREFIX + "/{authorId}", author.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void updateAuthor_returnHttpStatusNotFound_ifAuthorIsNotFound() throws Exception {
        AuthorDto authorDto = createAuthorDto(FIRSTNAME_AUTHOR_DTO, LASTNAME_AUTHOR_DTO);
        String authorDtoJson = mapper.writeValueAsString(authorDto);

        mockMvc.perform(put(URL_AUTHOR_PREFIX + "/{authorId}", 100l)
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

    private Author createAuthor() {
        return createAuthor(FIRSTNAME_AUTHOR, LASTNAME_AUTHOR);
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

    private User createUser() {
        return createUser(FIRSTNAME_USER, LASTNAME_USER,
                "adam95@gmail.com", ADDRESS, PASSWORD, UserRole.ADMINISTRATOR);
    }

    private User createUser(UserRole role) {
        return createUser(FIRSTNAME_USER, LASTNAME_USER,
                "adam995@gmail.com", ADDRESS, PASSWORD, role);
    }

}
