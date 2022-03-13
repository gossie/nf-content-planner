package com.github.gossie.nf.planner.login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/github")
public class GithubController {

    private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";

    private final RestTemplate restTemplate;
    private final String githubClientId;
    private final String githubAuthSecret;

    GithubController(RestTemplate restTemplate, @Value("${github.client.id}") String githubClientId, @Value("${github.auth.secret}") String githubAuthSecret) {
        this.restTemplate = restTemplate;
        this.githubClientId = githubClientId;
        this.githubAuthSecret = githubAuthSecret;
    }

    @GetMapping
    public void callbackUrl(@RequestParam String code) {
        ResponseEntity<GitHubResponse> accessTokenResponse = restTemplate.postForEntity(ACCESS_TOKEN_URL + "?client_id=" + githubClientId + "&client_secret=" + githubAuthSecret + "&code=" + code, null, GitHubResponse.class);

    }

}
