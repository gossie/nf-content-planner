package com.github.gossie.nf.planner.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class DefaultUserService implements UserService, UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultUserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(User user) {
        if (user.githubId() != null) {
            User createdUser = createGitHubUser(user);
            LOG.info("user with id {} and email {} was created based on GitHub account with id {}", createdUser.id(), createdUser.email(), createdUser.githubId());
            return createdUser;
        } else {
            User createdUser = createOwnUser(user);
            LOG.info("user with id {} and email {} was created", createdUser.id(), createdUser.email());
            return createdUser;
        }
    }

    private User createOwnUser(User user) {
        userRepository.findByEmail(user.email()).ifPresent(u -> {
            throw new IllegalStateException("e-mail is present in database");
        });
        return userRepository.createUser(user.encodePassword(passwordEncoder.encode(user.password())));
    }

    private User createGitHubUser(User user) {
        if (user.email() != null) {
            return userRepository.findByEmail(user.email())
                    .map(savedUser -> savedUser.merge(user))
                    .map(userRepository::saveUser)
                    .orElseGet(() -> userRepository.createUser(user));
        } else {
            return userRepository.createUser(user);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByGithubId(String githubId) {
        return userRepository.findByGithubId(githubId);
    }

    @Override
    public Optional<User> findUser(String which) {
        return findByEmail(which)
                .or(() -> findByGithubId(which));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(user.email(), user.password(), user.authorities().stream().map(SimpleGrantedAuthority::new).toList()))
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found"));
    }
}
