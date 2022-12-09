package com.example.Library.service;

import com.example.Library.exception.BadRequestException;
import com.example.Library.exception.NotFoundException;
import com.example.Library.model.dto.BookRentalDto;
import com.example.Library.model.dto.UserCreateDto;
import com.example.Library.model.dto.UserDto;
import com.example.Library.model.entity.BookRental;
import com.example.Library.model.entity.User;
import com.example.Library.model.mapper.BookRentalMapper;
import com.example.Library.model.mapper.UserMapper;
import com.example.Library.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        if (userWithEmailExists(userCreateDto.getEmail())) {
            throw new BadRequestException(String.format("User with email %s  exists", userCreateDto.getEmail()));
        }
        User user = UserMapper.toEntity(userCreateDto);
        user.setPassword(userCreateDto.getPassword());

        user.setCreatedAt(LocalDateTime.now());
        try {
            user.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        }catch(Exception ignored) {
            //exception will be trown only on user registration
        }
        return UserMapper.toUserCreateDto(userRepository.save(user));
    }

    public boolean userWithEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public List<BookRentalDto> getRentedBooks(Long userId) {
        List<BookRental> bookRentals = userRepository.getBookRentals(userId);
        return bookRentals.stream()
                .map(BookRentalMapper::toDto)
                .collect(Collectors.toList());
    }

    public User findUserModel(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s is not found", userId)));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s is not found", email)));
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDto getUser(Long userId) {
        User user = findUserModel(userId);

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
    public void deleteUser(Long userId) {
        User user = findUserModel(userId);
        if (!userRentedBooks(userId)) {
            userRepository.delete(user);
        } else {
            throw new BadRequestException("User have rented books");
        }
    }

    private boolean userRentedBooks(Long userId) {
        return userRepository.userHaveRentedBooks(userId);
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
