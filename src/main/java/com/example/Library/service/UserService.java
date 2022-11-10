package com.example.Library.service;

import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserCreateDto createUser(UserCreateDto userCreateDto);

    UserDto getUser(Long id);

    List<UserDto> getUsers();

    void deleteUser(Long id);

    UserDto updateUser(UserDto userDto, Long userId);

}
