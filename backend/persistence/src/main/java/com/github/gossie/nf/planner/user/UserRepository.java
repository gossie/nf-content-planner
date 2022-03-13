package com.github.gossie.nf.planner.user;

import org.springframework.stereotype.Component;

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

}
