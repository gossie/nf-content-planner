package com.github.gossie.nf.planner.course;

import com.github.gossie.nf.planner.user.User;
import com.github.gossie.nf.planner.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
class DefaultCourseService implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public DefaultCourseService(CourseRepository courseRepository,UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Course createCourse(Course course) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(IllegalStateException::new);
        return courseRepository.create(course, user);
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
                .map(course -> course.vote(topicId, user.id()))
                .map(courseRepository::save);
    }

    @Override
    public long determineNumberOfLeftVotes(String id, User user) {
        return 3 - courseRepository.get(id).stream()
                .flatMap(course -> course.topics().stream())
                .filter(t -> t.votes() != null)
                .flatMap(topic -> topic.votes().stream())
                .filter(userId -> Objects.equals(userId, user.id()))
                .count();
    }

    private boolean checkIfVotesAreLeft(Course course, User user) {
        return course.topics().stream()
                .filter(t -> t.votes() != null)
                .flatMap(t -> t.votes().stream())
                .filter(id -> Objects.equals(id, user.id()))
                .count() < 3;
    }
}
