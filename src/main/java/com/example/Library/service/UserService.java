package com.example.Library.service;

import com.example.Library.model.dto.BookRentalDto;
import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import com.example.Library.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserCreateDto createUser(UserCreateDto userCreateDto);

    UserDto getUser(Long userId);

    List<UserDto> getUsers();

    void deleteUser(Long userId);

    UserDto updateUser(UserDto userDto, Long userId);

    User findUserModel(Long userId);

    User getUserByEmail(String email);

    User saveUser(User user);

    boolean userWithEmailExists(String email);

    List<BookRentalDto> getRentedBooks(Long userId);
}
