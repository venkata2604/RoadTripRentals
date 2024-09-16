package com.carrental.serviceImpl;

import com.carrental.dto.UserDto;
import com.carrental.entity.UserEntity;
import com.carrental.exception.UserNotFoundException;
import com.carrental.repository.UserRepository;
import com.carrental.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UserDto> getUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(userEntity -> modelMapper.map(userEntity, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long id) throws UserNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isEmpty()) {
            throw new UserNotFoundException("User not found");
        } else {
            return modelMapper.map(userEntity.get(), UserDto.class);
        }
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return modelMapper.map(savedUserEntity, UserDto.class);
    }

    @Override
    public UserDto updateUser(Long id, UserDto updatedUserDto) throws UserNotFoundException {
        Optional<UserEntity> existingUserEntity = userRepository.findById(id);
        if (existingUserEntity.isEmpty()) throw new UserNotFoundException("User not found");

        UserEntity updatedUserEntity = existingUserEntity.get();
        updatedUserEntity.setEmail(updatedUserDto.getEmail());
        updatedUserEntity.setFirstName(updatedUserDto.getFirstName());
        updatedUserEntity.setLastName(updatedUserDto.getLastName());
        if(updatedUserDto.getPassword() != null) updatedUserEntity.setPassword(updatedUserDto.getPassword());

        updatedUserEntity.setUserId(id);
        userRepository.save(updatedUserEntity);

        return modelMapper.map(updatedUserEntity, UserDto.class);
    }

    @Override
    public void deleteUser(Long id) throws UserNotFoundException {
        Optional<UserEntity> existingUserEntity = userRepository.findById(id);
        if (existingUserEntity.isEmpty()) throw new UserNotFoundException("User not found");

        userRepository.deleteById(id);
    }

    @Override
    public UserDto findByEmail(String email) throws UserNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            return modelMapper.map(userEntity.get(), UserDto.class);
        } else {
            throw new UserNotFoundException("User not found");
        }
    }


    @Override
    public UserDto login(UserDto userDto) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (userDto.getPassword() != null && userDto.getPassword().equals(userEntity.getPassword())) {
            return modelMapper.map(userEntity, UserDto.class);
        }
        throw new UserNotFoundException("Invalid password");
    }

}
