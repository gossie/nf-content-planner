package com.github.gossie.nf.planner;

import org.springframework.stereotype.Component;

@Component
class DefaultCourseRepository implements CourseRepository {
    private final CourseDocumentRepository courseDocumentRepository;
    private final CourseMapper courseMapper;

    public DefaultCourseRepository(CourseDocumentRepository courseDocumentRepository, CourseMapper courseMapper) {
        this.courseDocumentRepository = courseDocumentRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public Course create(Course course) {
        return courseMapper.map(courseDocumentRepository.save(courseMapper.map(course)));
    }
}