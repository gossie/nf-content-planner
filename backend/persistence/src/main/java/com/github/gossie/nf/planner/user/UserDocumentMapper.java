package com.github.gossie.nf.planner.user;

import org.springframework.stereotype.Component;

@Component
class UserDocumentMapper {

    User map(UserDocument user) {
        return new User(user.id(), user.githubUsername());
    }

    UserDocument map(User user) {
        return new UserDocument(user.id(), user.githubUsername());
    }

}