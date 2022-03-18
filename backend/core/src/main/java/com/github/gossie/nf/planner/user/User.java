package com.github.gossie.nf.planner.user;

import java.util.List;

public record User(String id, String email, String firstname, String lastname, String password, String githubId, List<String> authorities) {
}
