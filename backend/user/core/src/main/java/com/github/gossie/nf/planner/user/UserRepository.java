package com.github.gossie.nf.planner.user;

import java.util.Optional;

public interface UserRepository {

    User createUser(User user);

    User saveUser(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByGithubId(String githubId);
}
