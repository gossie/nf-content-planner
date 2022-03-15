package com.github.gossie.nf.planner.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserController(UserService userService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO user) {
        if (!Objects.equals(user.password(), user.passwordAgain())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            User createdUser = userService.createUser(new User(null, user.email(), passwordEncoder.encode(user.password()), null, List.of("USER")));
            return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(createdUser.email(), "", ""));
        } catch(IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginData loginData) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginData.email(), loginData.password()));
            return jwtUtils.createToken(new HashMap<>(), loginData.email());
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credentials");
        }
    }
}
