package com.example.cloudstorage.controller;

import com.example.cloudstorage.dtos.UserDto;
import com.example.cloudstorage.model.Token;
import com.example.cloudstorage.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private AuthenticationService authenticationService;

    @Test
    void authenticationLogin() throws Exception {
        UserDto userDto = UserDto.builder().login("test-user").password("user1111").build();
        Token token = new Token("test-token");
        Mockito.when(authenticationService.authenticationLogin(userDto)).thenReturn(token);
        mockMvc.perform(post("/login")
                        .header("test-token", "test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void logout() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(request.getHeader("test-token")).thenReturn("test-token");
        AuthenticationController authenticationController = new AuthenticationController(authenticationService);
        Assertions.assertDoesNotThrow(() -> authenticationController.logout("test-token", request, response));
        Mockito.verify(authenticationService, Mockito.times(1)).logout("test-token", request, response);

    }

}
