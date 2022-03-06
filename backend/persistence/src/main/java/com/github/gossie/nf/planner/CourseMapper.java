package com.github.gossie.nf.planner;

import org.springframework.stereotype.Component;

@Component
class CourseMapper {
    public CourseDocument map(Course course) {
        return new CourseDocument(course.id(), course.name());
    }

    public Course map(CourseDocument course) {
        return new Course(course.getId(), course.getName());
    }
}
