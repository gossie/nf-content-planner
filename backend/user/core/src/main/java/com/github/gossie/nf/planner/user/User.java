package com.github.gossie.nf.planner.user;

import java.util.List;

public record User(String id, String email, String firstname, String lastname, String password, String githubId, List<String> authorities) {
    public User merge(User user) {
        return new User(id, email, firstname, lastname, password, user.githubId(), authorities);
    }
}
