package com.example.cloudstorage.service;

import com.example.cloudstorage.exception.WrongInfoException;
import com.example.cloudstorage.security.Role;
import com.example.cloudstorage.dtos.UserDto;
import com.example.cloudstorage.model.User;
import com.example.cloudstorage.repository.UserRepository;
import com.example.cloudstorage.utils.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RegisterService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public RegisterService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto registerUser(UserDto userDto) throws WrongInfoException {
        User user = userMapper.toEntity(userDto);
        if (userRepository.findUserByLogin(user.getLogin()) == null) {


            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Collections.singleton(Role.ADMIN_ROLE));
            user.setRole(Role.USER_ROLE.getAuthority());
            return userMapper.toDto(userRepository.save(user));
        } else throw new WrongInfoException("Incorrect login");
    }
}
