package com.github.gossie.nf.planner;

import org.springframework.stereotype.Component;

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
    public Course create(Course course) {
        return courseMapper.map(courseDocumentRepository.save(courseMapper.map(course)));
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
}