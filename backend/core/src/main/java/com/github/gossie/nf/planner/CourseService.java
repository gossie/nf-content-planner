package com.github.gossie.nf.planner;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    Course createCourse(Course course);

    Optional<Course> determineCourse(String id);

    Optional<Topic> createTopic(String courseId, Topic topic);

    List<Course> determineCourses();

    void deleteTopic(String courseId, String topicId);
}
