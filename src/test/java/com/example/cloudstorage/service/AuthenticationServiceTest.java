package com.example.cloudstorage.service;

import com.example.cloudstorage.dtos.UserDto;
import com.example.cloudstorage.model.Token;
import com.example.cloudstorage.model.User;
import com.example.cloudstorage.repository.UserRepository;
import com.example.cloudstorage.security.JWTToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AuthenticationServiceTest {
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private JWTToken jwtToken;
    @Autowired
    private AuthenticationService authenticationService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserDto userDto;
    private User user;

    @BeforeEach
    public void init() {
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);

        userDto = UserDto.builder()
                .login("login-test")
                .password("login-test")
                .build();
        user = User.builder()
                .id(1L)
                .login("login-test")
                .password("login-test")
                .build();
    }


    @Test
    void authenticationLogin() throws Exception {
        Mockito.doReturn(user).when(userRepository.findUserByLogin(userDto.getLogin()));
        Mockito.when(passwordEncoder.matches(userDto.getPassword(), user.getPassword())).thenReturn(true);
        Mockito.doReturn(user).when(jwtToken.getAuthenticatedUser());
        Token token = authenticationService.authenticationLogin(userDto);
        Assertions.assertNotNull(token);

    }

    @Test
    void logout() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Mockito.doReturn(user).when(userRepository.findUserByLogin(auth.getPrincipal().toString()));
        String login = authenticationService.logout("demo-tocken", request, response);
        Assertions.assertEquals(login, userDto.getLogin());
    }
}
