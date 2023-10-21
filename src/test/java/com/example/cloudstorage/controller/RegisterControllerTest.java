package com.example.cloudstorage.controller;

import com.example.cloudstorage.dtos.UserDto;
import com.example.cloudstorage.exception.WrongInfoException;
import com.example.cloudstorage.service.CloudServiceImpl;
import com.example.cloudstorage.service.RegisterService;
import com.example.cloudstorage.utils.UserMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RegisterControllerTest {

    private MockMvc mockMvc;
    @MockBean
    private UserMapper userMapper;
    private RegisterService registerService;

    @BeforeEach
    void setUp() {
        registerService = mock(RegisterService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new RegisterController(registerService)).build();
    }

    @SneakyThrows
    @Test
    void saveNewCandidate() throws WrongInfoException {
        UserDto userDto = UserDto.builder()
                .login("test-login").password("test-login").build();
        Mockito.when(registerService.registerUser(userDto)).thenReturn(userDto);
        mockMvc.perform(post("/user/register")
                        .header("new-token", "new-value-token")
                        .content(String.valueOf(userMapper.toEntity(userDto))))
                .andExpect(status().isOk());

    }
}