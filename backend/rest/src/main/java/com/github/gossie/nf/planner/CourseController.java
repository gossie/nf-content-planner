package com.github.gossie.nf.planner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin
public class CourseController {

    private final CourseService courseService;
    private final CourseDTOMapper courseMapper;

    public CourseController(CourseService courseService, CourseDTOMapper courseMapper) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
    }

    @GetMapping
    public List<CourseDTO> getCourses() {
        return courseService.determineCourses().stream()
                .map(courseMapper::map)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable String id) {
        return ResponseEntity.of(courseService.determineCourse(id)
                .map(courseMapper::map));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createCourse(@RequestBody CourseDTO course) {
        var createdCourse = courseService.createCourse(courseMapper.map(course));
        return ResponseEntity
                .created(URI.create("/api/courses/" + createdCourse.id()))
                .build();
    }

}
