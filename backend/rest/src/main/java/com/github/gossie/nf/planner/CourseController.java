package com.github.gossie.nf.planner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseDTOMapper courseMapper;

    public CourseController(CourseService courseService, CourseDTOMapper courseMapper) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createCourse(@RequestBody CourseDTO course) {
        var createdCourse = courseService.createCourse(courseMapper.map(course));
        return ResponseEntity
                .created(URI.create("/api/courses/" + createdCourse.id()))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable String id) {
        return ResponseEntity.of(courseService.determineCourse(id)
                .map(courseMapper::map));
    }

}
