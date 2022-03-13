package com.github.gossie.nf.planner;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    Course create(Course course);

    Optional<Course> get(String id);

    Course save(Course course);

    List<Course> getAll();

    Optional<Course> deleteCourse(String id);
}
