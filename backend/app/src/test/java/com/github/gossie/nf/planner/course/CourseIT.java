package com.github.gossie.nf.planner.course;

import com.github.gossie.nf.planner.user.LoginData;
import com.github.gossie.nf.planner.user.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void integrationTest() {
        ResponseEntity<UserDTO> createUserResponse = restTemplate.postForEntity("/api/users", new UserDTO("test@email.de", "123456a", "123456a"), UserDTO.class);
        assertThat(createUserResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/api/users/login", new LoginData("test@email.de", "123456a"), String.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotBlank();

        ResponseEntity<Void> createCourseResponse = restTemplate.exchange(
                "/api/courses",
                HttpMethod.POST,
                new HttpEntity<>(new CourseInput("hh-java-22-1"), createHeaders(loginResponse.getBody())),
                Void.class
        );
        assertThat(createCourseResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var location = createCourseResponse.getHeaders().getLocation();

        ResponseEntity<CourseDTO> getCourseResponse = restTemplate.exchange(
                location,
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(loginResponse.getBody())),
                CourseDTO.class
        );
        assertThat(getCourseResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getCourseResponse.getBody().name()).isEqualTo("hh-java-22-1");

        ResponseEntity<CourseDTO> notFoundResponse = restTemplate.exchange(
                "/api/courses/notFound",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(loginResponse.getBody())),
                CourseDTO.class
        );
        assertThat(notFoundResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);


        ResponseEntity<Void> topicPostResponse = restTemplate.exchange(
                location.toString() + "/topics",
                HttpMethod.POST,
                new HttpEntity<>(new TopicInput("Bitweise Operatoren", "Dies und das"), createHeaders(loginResponse.getBody())),
                Void.class
        );
        assertThat(topicPostResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<Void> notFoundPostResponse = restTemplate.exchange(
                "/api/courses/notFound/topics",
                HttpMethod.POST,
                new HttpEntity<>(new TopicInput("Bitweise Operatoren", "Dies und das"), createHeaders(loginResponse.getBody())),
                Void.class
        );
        assertThat(notFoundPostResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        ResponseEntity<CourseDTO> getCourseResponse2 = restTemplate.exchange(
                location,
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(loginResponse.getBody())),
                CourseDTO.class
        );
        assertThat(getCourseResponse2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getCourseResponse2.getBody().topics().get(0).name()).isEqualTo("Bitweise Operatoren");
        assertThat(getCourseResponse2.getBody().topics().get(0).description()).isEqualTo("Dies und das");
    }

    private HttpHeaders createHeaders(String token){
        String authHeader = "Bearer " + token;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        return headers;
    }

}
