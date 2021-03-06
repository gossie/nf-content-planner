package com.github.gossie.nf.planner.user.oauth;

import com.github.gossie.nf.planner.user.JwtUtils;
import com.github.gossie.nf.planner.user.User;
import com.github.gossie.nf.planner.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/github")
public class GithubController {

    private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";

    private final RestTemplate restTemplate;
    private final String githubClientId;
    private final String githubAuthSecret;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    GithubController(RestTemplate restTemplate, @Value("${github.client.id}") String githubClientId, @Value("${github.client.secret}") String githubAuthSecret, UserService userService, JwtUtils jwtUtils) {
        this.restTemplate = restTemplate;
        this.githubClientId = githubClientId;
        this.githubAuthSecret = githubAuthSecret;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public String callbackUrl(@RequestParam String code, Model model) {
        ResponseEntity<GitHubResponse> accessTokenResponse = restTemplate.postForEntity(ACCESS_TOKEN_URL + "?client_id=" + githubClientId + "&client_secret=" + githubAuthSecret + "&code=" + code, null, GitHubResponse.class);

        ResponseEntity<GitHubUser> userResponse = restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(accessTokenResponse.getBody().accessToken())),
                GitHubUser.class
        );

        userService.findByGithubId(userResponse.getBody().id())
                .orElseGet(() -> userService.createUser(new User(null, userResponse.getBody().email(), getFirstname(userResponse.getBody().name()), getLastname(userResponse.getBody().name()), null, userResponse.getBody().id(), List.of("USER"))));

        model.addAttribute("jwt", jwtUtils.createToken(new HashMap<>(), userResponse.getBody().id()));

        return "oauth-landing";
    }

    private String getFirstname(String name) {
        if (name == null) {
            return null;
        }

        String[] nameComponents = name.split(" ");
        if (nameComponents.length > 1) {
            return Arrays.stream(nameComponents).limit(nameComponents.length-1).collect(Collectors.joining(" "));
        }
        return name;
    }

    private String getLastname(String name) {
        if (name == null) {
            return null;
        }

        String[] nameComponents = name.split(" ");
        if (nameComponents.length > 1) {
            return nameComponents[nameComponents.length-1];
        }
        return name;
    }

    HttpHeaders createHeaders(String token){
        return new HttpHeaders() {{
            String authHeader = "Bearer " + token;
            set( "Authorization", authHeader );
        }};
    }

}
