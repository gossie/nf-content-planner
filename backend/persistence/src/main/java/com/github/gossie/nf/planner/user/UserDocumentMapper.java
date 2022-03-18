package com.github.gossie.nf.planner.user;

import org.springframework.stereotype.Component;

@Component
class UserDocumentMapper {

    User map(UserDocument user) {
        return new User(user.id(), user.email(), user.firstname(), user.lastname(), user.password(), user.githubId(), user.authorities());
    }

    UserDocument map(User user) {
        return new UserDocument(user.id(), user.email(), user.firstname(), user.firstname(), user.password(), user.githubId(), user.authorities());
    }

}
