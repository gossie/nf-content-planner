package com.github.gossie.nf.planner.login;

import com.github.gossie.nf.planner.user.User;
import com.github.gossie.nf.planner.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
    private final UserService userService;

    GithubController(RestTemplate restTemplate, @Value("${github.client.id}") String githubClientId, @Value("${github.auth.secret}") String githubAuthSecret, UserService userService) {
        this.restTemplate = restTemplate;
        this.githubClientId = githubClientId;
        this.githubAuthSecret = githubAuthSecret;
        this.userService = userService;
    }

    @GetMapping
    public String callbackUrl(@RequestParam String code) {
        ResponseEntity<GitHubResponse> accessTokenResponse = restTemplate.postForEntity(ACCESS_TOKEN_URL + "?client_id=" + githubClientId + "&client_secret=" + githubAuthSecret + "&code=" + code, null, GitHubResponse.class);

        ResponseEntity<GitHubUser> userResponse = restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(accessTokenResponse.getBody().accessToken())),
                GitHubUser.class
        );

        userService.createUser(new User(null, userResponse.getBody().login()));
        return return "forward:/index.html/courses";
    }

    HttpHeaders createHeaders(String token){
        return new HttpHeaders() {{
            String authHeader = "Bearer " + token;
            set( "Authorization", authHeader );
        }};
    }

}
