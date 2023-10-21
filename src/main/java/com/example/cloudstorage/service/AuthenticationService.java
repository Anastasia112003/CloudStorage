package com.example.cloudstorage.service;


import com.example.cloudstorage.dtos.UserDto;
import com.example.cloudstorage.exception.WrongInfoException;
import com.example.cloudstorage.model.Token;
import com.example.cloudstorage.model.User;
import com.example.cloudstorage.repository.UserRepository;
import com.example.cloudstorage.security.JWTToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JWTToken jwtToken;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, JWTToken jwtToken, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtToken = jwtToken;
        this.passwordEncoder = passwordEncoder;
    }

    public Token authenticationLogin(UserDto userDto) throws Exception {
        final User userFromDatabase = userRepository.findUserByLogin(userDto.getLogin());
        if (passwordEncoder.matches(userDto.getPassword(), userFromDatabase.getPassword())) {
            final String token = jwtToken.generateToken(userFromDatabase);
            return new Token(token);

        } else throw new WrongInfoException("Incorrect password");
    }

    public String logout(String authToken, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByLogin(auth.getPrincipal().toString());
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        if (user == null) {
            return null;
        }
        securityContextLogoutHandler.logout(request, response, auth);
        jwtToken.removeToken(authToken);
        return user.getLogin();
    }
}
