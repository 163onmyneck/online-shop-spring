package com.example.demo.service.user.impl;

import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.exception.RegistrationException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
                                    throws RegistrationException {
        if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
            throw new RegistrationException("Invalid repeated password");
        }

        if (userRepository.findByEmail(requestDto.getEmail()).isEmpty()) {
            return userMapper.toDto(userRepository.save(userMapper.toModel(requestDto)));
        }
        throw new RegistrationException("Can not register user with email: "
            + requestDto.getEmail() + " User with this email already exists");
    }
}
