package com.github.gossie.nf.planner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NFContentPlannerApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
		ResponseEntity<Void> postResponse = restTemplate.postForEntity("/api/courses", new CourseDTO(null, "hh-java-22-1", new ArrayList<>()), Void.class);
		assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		var location = postResponse.getHeaders().getLocation();

		ResponseEntity<CourseDTO> getResponse1 = restTemplate.getForEntity(location, CourseDTO.class);
		assertThat(getResponse1.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse1.getBody().name()).isEqualTo("hh-java-22-1");

		ResponseEntity<CourseDTO> notFoundResponse = restTemplate.getForEntity("/api/courses/notFound", CourseDTO.class);
		assertThat(notFoundResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		ResponseEntity<Void> topicPostResponse = restTemplate.postForEntity(location.toString() + "/topics", new TopicDTO(null, "Bitweise Operatoren", "Dies und das"), Void.class);
		assertThat(topicPostResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		ResponseEntity<Void> notFoundPostResponse = restTemplate.postForEntity("/api/courses/notFound/topics", new TopicDTO(null, "Bitweise Operatoren", "Dies und das"), Void.class);
		assertThat(notFoundPostResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		ResponseEntity<CourseDTO> getResponse2 = restTemplate.getForEntity(location, CourseDTO.class);
		assertThat(getResponse2.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse2.getBody().topics().get(0).name()).isEqualTo("Bitweise Operatoren");
		assertThat(getResponse2.getBody().topics().get(0).description()).isEqualTo("Dies und das");
	}

}
