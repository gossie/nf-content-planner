package com.github.gossie.nf.planner.course;

import com.github.gossie.nf.planner.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Override
    public Optional<Course> determineCourse(String id) {
        return courseRepository.get(id);
    }

    @Override
    public Optional<Topic> createTopic(String courseId, Topic topic) {
        return courseRepository.get(courseId)
                .map(course -> {
                    Topic createdTopic = course.addTopic(topic);
                    courseRepository.save(course);
                    return createdTopic;
                });
    }

    @Override
    public List<Course> determineCourses() {
        return courseRepository.getAll();
    }

    @Override
    public void deleteTopic(String courseId, String topicId) {
        courseRepository.get(courseId)
                .map(course -> course.removeTopic(topicId))
                .ifPresent(courseRepository::save);
    }

    @Override
    public Optional<Course> deleteCourse(String id) {
        return courseRepository.deleteCourse(id);
    }

    @Override
    public Optional<Course> vote(String courseId, String topicId, User user) {
        return courseRepository.get(courseId)
                .filter(course -> checkIfVotesAreLeft(course, user))
                .map(course -> course.vote(topicId, user.id()));
    }

    private boolean checkIfVotesAreLeft(Course course, User user) {
        return course.topics().stream()
                .flatMap(t -> t.votes().stream())
                .filter(id -> Objects.equals(id, user.id()))
                .count() < 3;
    }
}
