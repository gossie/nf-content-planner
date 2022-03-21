package com.github.gossie.nf.planner.course;

import com.github.gossie.nf.planner.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class DefaultCourseRepository implements CourseRepository {
    private final CourseDocumentRepository courseDocumentRepository;
    private final CourseDocumentMapper courseMapper;

    public DefaultCourseRepository(CourseDocumentRepository courseDocumentRepository, CourseDocumentMapper courseMapper) {
        this.courseDocumentRepository = courseDocumentRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public Course create(Course course, User user) {
        return courseMapper.map(courseDocumentRepository.save(courseMapper.map(course, user)));
    }

    @Override
    public Optional<Course> get(String id) {
        return courseDocumentRepository.findById(id)
                .map(courseMapper::map);
    }

    @Override
    public Course save(Course course) {
        return courseMapper.map(courseDocumentRepository.save(courseMapper.map(course)));
    }

    @Override
    public List<Course> getAll() {
        return courseDocumentRepository.findAll().stream()
                .map(courseMapper::map)
                .toList();
    }

    @Override
    public Optional<Course> deleteCourse(String id) {
        return courseDocumentRepository.findById(id)
                .map(course -> {
                    courseDocumentRepository.delete(course);
                    return courseMapper.map(course);
                });
    }
}