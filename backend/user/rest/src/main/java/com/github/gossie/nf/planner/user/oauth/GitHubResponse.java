package com.github.gossie.nf.planner.user.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("scope") String scope,
        @JsonProperty("token_type") String tokenType
) {

}
