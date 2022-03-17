package com.github.gossie.nf.planner.course;

import com.github.gossie.nf.planner.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final UserService userService;
    private final CourseService courseService;
    private final CourseDTOMapper courseMapper;

    public CourseController(UserService userService, CourseService courseService, CourseDTOMapper courseMapper) {
        this.userService = userService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseDTO> deleteCourse(@PathVariable String id) {
        return ResponseEntity.of(courseService.deleteCourse(id)
                .map(courseMapper::map));
    }

    @GetMapping("/{id}/votes")
    public ResponseEntity<Long> getNumberOfLeftVotes(@PathVariable String id, Principal principal) {
        return ResponseEntity.of(userService.findByEmail(principal.getName())
                .or(() -> userService.findByGithubId(principal.getName()))
                .map(user -> courseService.determineNumberOfLeftVotes(id, user)));
    }

}
