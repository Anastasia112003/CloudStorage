package com.example.cloudstorage.controller;

import com.example.cloudstorage.dtos.UserDto;
import com.example.cloudstorage.exception.WrongInfoException;
import com.example.cloudstorage.service.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> saveNewCandidate(UserDto userDto) {
        try {
            return new ResponseEntity<>(registerService.registerUser(userDto), HttpStatus.OK);
        } catch (WrongInfoException e) {
            throw new RuntimeException(e);
        }

    }
}
