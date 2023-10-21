package com.example.cloudstorage.controller;

import com.example.cloudstorage.config.AuthenticationConstant;
import com.example.cloudstorage.dtos.UserDto;
import com.example.cloudstorage.model.Token;
import com.example.cloudstorage.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/login")
    public ResponseEntity<Token> authenticationLogin(@RequestBody UserDto userDto) throws Exception {
        try {
            Token token = authenticationService.authenticationLogin(userDto);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = AuthenticationConstant.AUTH_TOKEN) String authToken, HttpServletRequest request, HttpServletResponse response) {
        String userLogout = authenticationService.logout(authToken, request, response);
        if (userLogout == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

