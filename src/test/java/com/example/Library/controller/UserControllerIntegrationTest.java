package com.example.Library.controller;

import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import com.example.Library.model.entity.User;
import com.example.Library.model.entity.UserRole;
import com.example.Library.repository.UserRepository;
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
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    private String FIRSTNAME = "Marko";
    private String LASTNAME = "Adamovic";
    private String EMAIL = "markoadam1995@yahoo.com";
    private String ADDRESS = "Veternik";
    private String PASSWORD = "123";
    private UserRole USERROLE = UserRole.valueOf("USER");

    private String FIRSTNAME_DTO = "Nikola";
    private String LASTNAME_DTO = "Nikolic";
    private String EMAIL_DTO = "nikolicniko@gmail.com";
    private String ADDRESS_DTO = "NS";
    private UserRole USERROLE_DTO = UserRole.valueOf("USER");

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void createUser_returnHttpStatusCreated() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto(FIRSTNAME, LASTNAME, EMAIL, ADDRESS, PASSWORD, USERROLE);
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
        User user = createUser(FIRSTNAME, LASTNAME, EMAIL, ADDRESS, PASSWORD, USERROLE);
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
        User user = createUser(FIRSTNAME, LASTNAME, EMAIL, ADDRESS, PASSWORD, USERROLE);
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
        User user = createUser(FIRSTNAME, LASTNAME, EMAIL, ADDRESS, PASSWORD, USERROLE);
        UserDto userDto = createUserDto(FIRSTNAME_DTO, LASTNAME_DTO, EMAIL_DTO, ADDRESS_DTO, USERROLE_DTO);
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
        UserDto userDto = createUserDto(FIRSTNAME_DTO, LASTNAME_DTO, EMAIL_DTO, ADDRESS_DTO, USERROLE_DTO);
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
