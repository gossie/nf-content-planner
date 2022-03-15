package com.github.gossie.nf.planner.course;

import com.github.gossie.nf.planner.user.User;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    Course create(Course course, User user);

    Optional<Course> get(String id);

    Course save(Course course);

    List<Course> getAll();

    Optional<Course> deleteCourse(String id);
}
