package com.example.Library.model.mapper;

import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import com.example.Library.model.entity.User;

public class UserMapper {

    public static User toEntity(UserCreateDto userCreateDto) {
        return new User(userCreateDto.getFirstName(), userCreateDto.getLastName(), userCreateDto.getAdress(),
               userCreateDto.getEmail(), userCreateDto.getPassword(), userCreateDto.getUserRole());
    }

    public static UserCreateDto toUserCreateDto(User user) {
        return new UserCreateDto(user.getFirstName(), user.getLastName(), user.getEmail(), user.getAdress(),
                user.getPassword(), user.getUserType());
    }

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getAdress(), user.getUserType());
    }

}