package com.example.Library.service;

import com.example.Library.exception.BadRequestException;
import com.example.Library.exception.NotFoundException;
import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import com.example.Library.model.entity.User;
import com.example.Library.model.mapper.UserMapper;
import com.example.Library.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserCreateDto createUser(UserCreateDto userCreateDto) {

        if(userWithEmailExists(userCreateDto.getEmail())) {
            throw new BadRequestException("User with email " + userCreateDto.getEmail() + " exists");
        }
        User user = UserMapper.toEntity(userCreateDto);

        return UserMapper.toUserCreateDto(userRepository.save(user));
    }

    public boolean userWithEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User findUserModel(Long id) throws NotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    @Override
    public UserDto getUser(Long id) {
        User user = findUserModel(id);

        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        User user = findUserModel(id);
        userRepository.delete(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        User user = findUserModel(userId);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setAdress(userDto.getAddress());
        user.setUserType(userDto.getUserType());

        return UserMapper.toDto(userRepository.save(user));
    }

}
