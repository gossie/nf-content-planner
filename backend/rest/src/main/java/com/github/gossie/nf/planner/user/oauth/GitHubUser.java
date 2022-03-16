package com.github.gossie.nf.planner.user.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubUser(@JsonProperty("login") String login, @JsonProperty("email") String email) {
}
