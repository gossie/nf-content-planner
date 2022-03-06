package com.github.gossie.nf.planner;

import java.util.Optional;

public interface CourseService {
    Course createCourse(Course course);

    Optional<Course> determineCourse(String id);

    Optional<Topic> createTopic(String courseId, Topic topic);
}
