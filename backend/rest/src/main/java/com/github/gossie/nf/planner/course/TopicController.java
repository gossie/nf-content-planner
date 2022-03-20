package com.github.gossie.nf.planner.course;

import com.github.gossie.nf.planner.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("/api/courses/{courseId}/topics")
public class TopicController {

    private final UserService userService;
    private final CourseService courseService;
    private final CourseDTOMapper courseMapper;
    private final TopicDTOMapper topicMapper;

    public TopicController(UserService userService, CourseService courseService, CourseDTOMapper courseMapper, TopicDTOMapper topicMapper) {
        this.userService = userService;
        this.courseService = courseService;
        this.courseMapper = courseMapper;
        this.topicMapper = topicMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createTopic(@PathVariable String courseId, @RequestBody TopicDTO topic) {
        if (topic.name() == null || Objects.equals(topic.name(), "")) {
            return ResponseEntity.badRequest().build();
        }

        return courseService.createTopic(courseId, topicMapper.map(topic))
                .map(createdTopic -> ResponseEntity
                        .created(URI.create("/api/courses/" + courseId + "/topics/" + createdTopic.id()))
                        .build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{topicId}/votes")
    public ResponseEntity<CourseDTO> vote(@PathVariable String courseId, @PathVariable String topicId, Principal principal) {
        return ResponseEntity.of(userService.findUser(principal.getName())
                .flatMap(user -> courseService.vote(courseId, topicId, user).map(course -> courseMapper.map(course, user.id()))));
    }

    @DeleteMapping("/{topicId}/votes")
    public ResponseEntity<CourseDTO> removeVote(@PathVariable String courseId, @PathVariable String topicId, Principal principal) {
        return ResponseEntity.of(userService.findUser(principal.getName())
                .flatMap(user -> courseService.removeVote(courseId, topicId, user).map(course -> courseMapper.map(course, user.id()))));
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<CourseDTO> deleteTopic(@PathVariable String courseId, @PathVariable String topicId, Principal principal) {
        return userService.findUser(principal.getName())
                .flatMap(user -> courseService.deleteTopic(courseId, topicId, user.id()).map(course -> courseMapper.map(course, user.id())))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

}
