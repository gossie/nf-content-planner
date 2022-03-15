package com.github.gossie.nf.planner.course;

import com.github.gossie.nf.planner.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/api/courses/{courseId}/topics")
@CrossOrigin
public class TopicController {

    private final UserService userService;
    private final CourseService courseService;
    private final TopicDTOMapper topicMapper;

    public TopicController(UserService userService, CourseService courseService, TopicDTOMapper topicMapper) {
        this.userService = userService;
        this.courseService = courseService;
        this.topicMapper = topicMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createTopic(@PathVariable String courseId, @RequestBody TopicDTO topic) {
        return courseService.createTopic(courseId, topicMapper.map(topic))
                .map(createdTopic -> ResponseEntity
                        .created(URI.create("/api/courses/" + courseId + "/topics/" + createdTopic.id()))
                        .build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{topicId}")
    public void vote(@PathVariable String courseId, @PathVariable String topicId, Principal principal) {
        userService.findByEmail(principal.getName()).ifPresent(user -> {
            courseService.vote(courseId, topicId, user);
        });
    }

    @DeleteMapping("/{topicId}")
    public void deleteTopic(@PathVariable String courseId, @PathVariable String topicId) {
        courseService.deleteTopic(courseId, topicId);
    }

}