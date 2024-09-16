package com.carrental.service;

import com.carrental.dto.UserDto;
import com.carrental.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    UserDto getUser(Long id);

    UserDto addUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto updatedUserDto);

    void deleteUser(Long id);

    UserDto findByEmail(String email) throws UserNotFoundException;

    UserDto login(UserDto userDto) throws UserNotFoundException;
}
