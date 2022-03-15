package com.github.gossie.nf.planner.user;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository {

    User createUser(User user);

    Optional<User> findByEmail(String email);
}
