package com.github.gossie.nf.planner.user;

import java.util.List;

public record User(String id, String email, String password, String githubUsername, List<String> authorities) {
}
