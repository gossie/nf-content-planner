package com.github.gossie.nf.planner.user;

import org.springframework.stereotype.Service;

@Service
class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.createUser(user);

    }
}
