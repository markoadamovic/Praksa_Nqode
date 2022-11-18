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
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    ObjectMapper mapper = new ObjectMapper();

    private User user;

    @BeforeEach
    private void setUp() {
        user = createUser(FIRSTNAME_USER, LASTNAME_USER, EMAIL, ADDRESS, PASSWORD, USERROLE);
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
        mockMvc.perform(post("/user")
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
        mockMvc.perform(get("/user" + "/{id}", user.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getUser_returnHttpStatusNotFound_ifUserNotFound() throws Exception {
        mockMvc.perform(get("/user" + "{id}", 200L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsers_returnHttpStatusOK() throws Exception {
        mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_returnHttpStatusNoContent() throws Exception {
        mockMvc.perform(delete("/user" + "/{id}", user.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_returnHttpStatusIsNotFound_ifUserNotFound() throws Exception {
        mockMvc.perform(delete("/user" + "/{id}", 14L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_returnHttpStatusOk() throws Exception {
        UserDto userDto = createUserDto(UPDATE_FIRSTNAME, UPDATE_LASTNAME, UPDATE_EMAIL, UPDATE_ADDRESS, UPDATE_USERROLE);
        String userDtoJson = mapper.writeValueAsString(userDto);

        mockMvc.perform(put("/user" + "/{id}", user.getId())
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
        mockMvc.perform(put("/user" + "/{id}", 123L)
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
