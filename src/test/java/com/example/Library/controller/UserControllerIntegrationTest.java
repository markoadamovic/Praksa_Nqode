package com.example.Library.controller;

import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import com.example.Library.model.entity.User;
import com.example.Library.model.entity.UserRole;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static com.example.Library.utils.TestUtils.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("local")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private User user;

    @BeforeEach
    private void setUp() {
        user = createUser(FIRSTNAME_USER, LASTNAME_USER, EMAIL, ADDRESS, PASSWORD, USERROLE);

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
        userRepository.deleteAll();
    }

    @Test
    void createUser_returnHttpStatusCreated() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto(FIRSTNAME_USER, LASTNAME_USER, EMAIL_CREATE,
                                                        ADDRESS, PASSWORD, USERROLE);
        String userCreateDtoJson = mapper.writeValueAsString(userCreateDto);
        mockMvc.perform(post(URL_USER_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userCreateDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(userCreateDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userCreateDto.getLastName()));
    }

    @Test
    void getUser_returnHttpStatusOk() throws Exception {
        mockMvc.perform(get(URL_USER_PREFIX + "/{id}", user.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getUser_returnHttpStatusNotFound_ifUserNotFound() throws Exception {
        mockMvc.perform(get(URL_USER_PREFIX + "{id}", 200L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsers_returnHttpStatusOK() throws Exception {
        mockMvc.perform(get(URL_USER_PREFIX))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_returnHttpStatusNoContent() throws Exception {
        mockMvc.perform(delete(URL_USER_PREFIX + "/{id}", user.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_returnHttpStatusIsNotFound_ifUserNotFound() throws Exception {
        mockMvc.perform(delete(URL_USER_PREFIX + "/{id}", 14L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_returnHttpStatusOk() throws Exception {
        UserDto userDto = createUserDto(UPDATE_FIRSTNAME, UPDATE_LASTNAME, UPDATE_EMAIL, UPDATE_ADDRESS, UPDATE_USERROLE);
        String userDtoJson = mapper.writeValueAsString(userDto);

        mockMvc.perform(put(URL_USER_PREFIX + "/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(userDto.getFirstName()));
    }

    @Test
    void updateUser_returnHttpStatusNotFound_ifUserIsNotFound() throws Exception {
        UserDto userDto = createUserDto(UPDATE_FIRSTNAME, UPDATE_LASTNAME, UPDATE_EMAIL, UPDATE_ADDRESS, UPDATE_USERROLE);
        String userDtoJson = mapper.writeValueAsString(userDto);
        mockMvc.perform(put(URL_USER_PREFIX + "/{id}", 123L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
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

    private UserDto createUserDto(String firstName, String lastName,String email, String address, UserRole userRole) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setAddress(address);
        userDto.setEmail(email);
        userDto.setUserRole(userRole);

        return userDto;
    }
}
