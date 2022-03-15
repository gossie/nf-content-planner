package com.github.gossie.nf.planner.course;

import com.github.gossie.nf.planner.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/api/courses/{courseId}/topics")
@CrossOrigin("http://localhost:3000")
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
        return courseService.createTopic(courseId, topicMapper.map(topic))
                .map(createdTopic -> ResponseEntity
                        .created(URI.create("/api/courses/" + courseId + "/topics/" + createdTopic.id()))
                        .build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{topicId}")
    public ResponseEntity<CourseDTO> vote(@PathVariable String courseId, @PathVariable String topicId, Principal principal) {
        return ResponseEntity.of(userService.findByEmail(principal.getName())
                .flatMap(user -> courseService.vote(courseId, topicId, user))
                .map(courseMapper::map));
    }

    @DeleteMapping("/{topicId}")
    public void deleteTopic(@PathVariable String courseId, @PathVariable String topicId) {
        courseService.deleteTopic(courseId, topicId);
    }

}
