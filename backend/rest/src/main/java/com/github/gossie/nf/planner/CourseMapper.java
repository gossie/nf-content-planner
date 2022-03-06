package com.github.gossie.nf.planner;

import org.springframework.stereotype.Component;

@Component
class CourseMapper {
    public Course map(CourseDTO course) {
        return new Course(null, course.name());
    }
}
