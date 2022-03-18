package com.github.gossie.nf.planner.course;

import com.github.gossie.nf.planner.user.User;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    Course createCourse(Course course);

    Optional<Course> determineCourse(String id);

    Optional<Topic> createTopic(String courseId, Topic topic);

    List<Course> determineCourses();

    void deleteTopic(String courseId, String topicId, String userId);

    Optional<Course> deleteCourse(String id, String userId);

    Optional<Course> vote(String courseId, String topicId, User user);

    long determineNumberOfLeftVotes(String id, User user);

    Optional<Course> remove(String courseId, String topicId, User user);
}
