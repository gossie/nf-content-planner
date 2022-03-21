package com.github.gossie.nf.planner.user;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class DefaultUserRepository implements UserRepository {

    private final UserDocumentRepository userDocumentRepository;
    private final UserDocumentMapper userMapper;

    public DefaultUserRepository(UserDocumentRepository userDocumentRepository, UserDocumentMapper userMapper) {
        this.userDocumentRepository = userDocumentRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User createUser(User user) {
        return userMapper.map(userDocumentRepository.save(userMapper.map(user)));
    }

    @Override
    public User saveUser(User user) {
        return userMapper.map(userDocumentRepository.save(userMapper.map(user)));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDocumentRepository.findByEmail(email)
                .map(userMapper::map);
    }

    @Override
    public Optional<User> findByGithubId(String githubId) {
        return userDocumentRepository.findByGithubId(githubId)
                .map(userMapper::map);
    }

}
