package com.github.gossie.nf.planner.course;

import com.github.gossie.nf.planner.user.User;
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
    public ResponseEntity<List<CourseDTO>> getCourses(Principal principal) {
        return ResponseEntity.of(userService.findUser(principal.getName())
                .map(user -> courseService.determineCourses().stream()
                        .map(course -> courseMapper.map(course, user.id()))
                        .toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable String id, Principal principal) {
        return ResponseEntity.of(userService.findUser(principal.getName())
                .flatMap(user -> courseService.determineCourse(id)
                        .map(course -> courseMapper.map(course, user.id()))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createCourse(@RequestBody CourseDTO course, Principal principal) {
        return userService.findUser(principal.getName())
                .map(user -> new CourseDTO(course.id(), course.name(), course.topics(), user.id()))
                .map(courseMapper::map)
                .map(courseService::createCourse)
                .map(createdCourse -> ResponseEntity.created(URI.create("/api/courses/" + createdCourse.id())).<Void>build())
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseDTO> deleteCourse(@PathVariable String id, Principal principal) {
        return userService.findUser(principal.getName())
                .map(User::id)
                .flatMap(userId -> courseService.deleteCourse(id, userId).map(course -> courseMapper.map(course, userId)))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @GetMapping("/{id}/votes")
    public ResponseEntity<Long> getNumberOfLeftVotes(@PathVariable String id, Principal principal) {
        return ResponseEntity.of(userService.findUser(principal.getName())
                .map(user -> courseService.determineNumberOfLeftVotes(id, user)));
    }

}
