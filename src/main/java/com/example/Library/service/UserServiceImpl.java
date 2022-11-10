package com.example.Library.service;

import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import com.example.Library.model.entity.User;
import com.example.Library.model.mapper.UserMapper;
import com.example.Library.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserCreateDto createUser(UserCreateDto userCreateDto) {
        //TODO EXception if exists(user.email) throw...
        User user = UserMapper.toEntity(userCreateDto);

        return UserMapper.toUserCreateDto(userRepository.save(user));
    }

    public User findUserModel(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            throw new RuntimeException("User not found"); //TODO
        }
        return userOptional.get();
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
        user.setAdress(userDto.getAdress());
        user.setUserType(userDto.getUserType());

        return UserMapper.toDto(userRepository.save(user));
    }

}
