package com.github.gossie.nf.planner.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
public record UserDocument(@Id String id, String email, String password, String githubId, List<String> authorities) {
}
