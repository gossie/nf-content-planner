package com.github.gossie.nf.planner.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

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
            User createdUser = userService.createUser(new User(null, user.email(), user.firstname(), user.lastname(), passwordEncoder.encode(user.password()), null, List.of("USER")));
            return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(createdUser.email(), createdUser.firstname(), createdUser.lastname(), "", ""));
        } catch(IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginData loginData) {
        try {
            return ResponseEntity.of(userService.findByEmail(loginData.email())
                    .map(user -> createToken(loginData, user)));
        } catch(AuthenticationException e) {
            LOG.warn("user with email " + loginData.email() + " could not be authenticated", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credentials");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        return ResponseEntity.of(userService.findUser(principal.getName())
                .map(user -> new UserDTO(user.email(), user.firstname(), user.lastname(), "", "")));
    }

    private String createToken(LoginData loginData, User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginData.email(), loginData.password()));
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.id());
        return jwtUtils.createToken(claims, loginData.email());
    }
}
