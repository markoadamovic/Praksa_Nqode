package com.example.Library.service;

import com.example.Library.exception.BadRequestException;
import com.example.Library.exception.NotFoundException;
import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import com.example.Library.model.entity.User;
import com.example.Library.model.entity.UserRole;
import com.example.Library.model.mapper.UserMapper;
import com.example.Library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.Library.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    private User user;

    private User user1;

    private UserDto userDto;

    private UserDto userDto1;

    private UserCreateDto userCreateDto;

    private List<UserDto> userDtoList;

    private List<User> userList;

    @BeforeEach
    void setup() {
        user = createUser(1l, FIRSTNAME_USER, LASTNAME_USER, ADDRESS, EMAIL,
                PASSWORD, USERROLE);
        user1 = createUser(2l, FIRSTNAME_USER, LASTNAME_USER, ADDRESS, EMAIL2,
                PASSWORD, USERROLE);
        userDto = UserMapper.toDto(user);
        userDto1 = UserMapper.toDto(user1);
        userCreateDto = UserMapper.toUserCreateDto(user);
        userDtoList = createUserDtoList(userDto);
        userList = createUserList(user);
    }

    private List<User> createUserList(User user){

        return List.of(user);
    }

    private List<UserDto> createUserDtoList(UserDto userDto) {

        return List.of(userDto);
    }

    private User createUser(Long id, String firstName, String lastName, String address,
                       String email, String password, UserRole userRole) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAdress(address);
        user.setPassword(password);
        user.setUserType(userRole);

        return user;
    }

    @Test
    void shouldCreateUser() {
        Mockito.when(userRepository.findByEmail(userCreateDto.getEmail())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any())).thenReturn(user);

        UserCreateDto expected = userService.createUser(userCreateDto);
        assertEquals(user.getEmail(), expected.getEmail());
    }

    @Test
    void createUser_ifEmailIsInUse_throwBadRequestException() {
        Mockito.when(userRepository.findByEmail(userCreateDto.getEmail())).thenReturn(Optional.ofNullable(user));

        Exception exception = assertThrows(BadRequestException.class, () -> userService.createUser(userCreateDto));
        assertTrue(exception.getMessage().contains("exists"));
    }

    @Test
    void findUser_ifUserNotExists_throwNotFoundException() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> userService.findUserModel(any()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void findUser_ifUserExists() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));

        User expected = userService.findUserModel(user.getId());
        assertEquals(user.getId(), expected.getId());
    }

    @Test
    void getUser_ifUserExists() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));

        UserDto expected = userService.getUser(user.getId());
        assertEquals(user.getId(), expected.getId());
        assertEquals(user.getEmail(), expected.getEmail());
    }

    @Test
    void getUser_ifUserNotExists_throwNotFoundException() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> userService.getUser(any()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getUsers_ifUsersExists_returnUserDtoList() {
        Mockito.when(userRepository.findAll()).thenReturn(userList);

        List<UserDto> expected = userService.getUsers();
        assertEquals(userList.get(0).getId(), expected.get(0).getId());
    }

    @Test
    void deleteUser_ifUserExists() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));

        userService.deleteUser(user.getId());
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_ifUserNotExists_throwNotFoundException() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> userService.deleteUser(any()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void updateUser_ifUserExists_returnUserDto() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.save(any())).thenReturn(user);

        UserDto expected = userService.updateUser(userDto1, user.getId());
        assertEquals(user.getId(), expected.getId());
    }

    @Test
    void updateUser_ifUserNotExists_throwNotFoundException() {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundException.class, () -> userService.updateUser(userDto1, any()));
        assertTrue(exception.getMessage().contains("not found"));
    }
}
