package com.example.demo.service.user.impl;

import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.exception.RegistrationException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Role.RoleName;
import com.example.demo.model.User;
import com.example.demo.repository.role.RoleRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.user.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
                                    throws RegistrationException {
        if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
            throw new RegistrationException("Invalid repeated password");
        }

        if (userRepository.findByEmail(requestDto.getEmail()).isEmpty()) {
            requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
            User user = userMapper.toModel(requestDto);
            user.setRoles(Set.of(roleRepository.findByRoleName((RoleName.USER)).get()));
            return userMapper.toDto(userRepository.save(user));
        }
        throw new RegistrationException("Can not register user with email: "
            + requestDto.getEmail() + " User with this email already exists");
    }

    @Override
    public Long getUserIdByEmail(String email) {
        return userRepository.getIdByEmail(email);
    }
}
