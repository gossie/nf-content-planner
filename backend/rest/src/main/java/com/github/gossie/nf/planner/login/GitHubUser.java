package com.github.gossie.nf.planner.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubUser(@JsonProperty("login") String login) {
}
