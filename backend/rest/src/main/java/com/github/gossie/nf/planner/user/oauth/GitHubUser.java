package com.github.gossie.nf.planner.user.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubUser(
        @JsonProperty("id") String id,
        @JsonProperty("login") String login,
        @JsonProperty("email") String email,
        @JsonProperty("name") String name) {
}
