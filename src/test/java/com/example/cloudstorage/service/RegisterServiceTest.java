package com.example.cloudstorage.service;

import com.example.cloudstorage.dtos.UserDto;
import com.example.cloudstorage.exception.WrongInfoException;
import com.example.cloudstorage.model.User;
import com.example.cloudstorage.repository.UserRepository;
import com.example.cloudstorage.utils.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.example.cloudstorage.security.Role;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RegisterServiceTest {
    @Autowired
    private RegisterService registerService;
    @MockBean
    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserDto userDto;
    private User user;


    @BeforeEach
    public void init() {
        userDto = UserDto.builder()
                .login("LoginTest")
                .password("PasswordTest")
                .build();
        user = User.builder()
                .id(1L)
                .login("LoginTest")
                .password("PasswordTest")
                .roles(Collections.singleton(Role.USER_ROLE))
                .build();
    }

    @Test
    void registerUser() throws WrongInfoException {
        Mockito.doReturn(user).when(userMapper.toEntity(userDto));
        Mockito.doReturn(userRepository.findUserByLogin(user.getLogin()));
        registerService.registerUser(userDto);
        Mockito.verify(userRepository, Mockito.times(1)).findUserByLogin("user1");
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

}
