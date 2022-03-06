package com.github.gossie.nf.planner;

import org.springframework.stereotype.Service;

@Service
class DefaultCourseService implements CourseService {

    private final CourseRepository courseRepository;

    public DefaultCourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course createCourse(Course course) {
        return courseRepository.create(course);
    }
}
