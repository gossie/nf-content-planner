package com.github.gossie.nf.planner.user;

import java.util.Optional;

public interface UserService {

    User createUser(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByGithubId(String id);

    Optional<User> findUser(String which);
}
