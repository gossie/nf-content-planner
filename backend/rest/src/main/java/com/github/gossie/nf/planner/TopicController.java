package com.github.gossie.nf.planner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/courses/{courseId}/topics")
@CrossOrigin
public class TopicController {

    private final CourseService courseService;
    private final TopicDTOMapper topicMapper;

    public TopicController(CourseService courseService, TopicDTOMapper topicMapper) {
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

    @DeleteMapping("/{topicId}")
    public void deleteTopic(@PathVariable String courseId, @PathVariable String topicId) {
        courseService.deleteTopic(courseId, topicId);
    }

}
