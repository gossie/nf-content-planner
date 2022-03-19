package com.github.gossie.nf.planner.course;

import com.github.gossie.nf.planner.user.User;
import com.github.gossie.nf.planner.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
class DefaultCourseService implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final int votesPerUser;

    public DefaultCourseService(CourseRepository courseRepository, UserRepository userRepository, @Value("${app.user.max-votes}") int votesPerUser) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.votesPerUser = votesPerUser;
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
    public Optional<Course> deleteTopic(String courseId, String topicId, String userId) {
        return courseRepository.get(courseId)
                .filter(course -> Objects.equals(course.userId(), userId))
                .map(course -> course.removeTopic(topicId))
                .map(courseRepository::save);
    }

    @Override
    public Optional<Course> deleteCourse(String id, String userId) {
        return courseRepository.get(id)
                .filter(course -> Objects.equals(course.userId(), userId))
                .flatMap(course -> courseRepository.deleteCourse(course.id()));
    }

    @Override
    public Optional<Course> vote(String courseId, String topicId, User user) {
        return courseRepository.get(courseId)
                .filter(course -> checkIfVotesAreLeft(course, user))
                .map(course -> course.vote(topicId, user.id()))
                .map(courseRepository::save);
    }

    @Override
    public Optional<Course> removeVote(String courseId, String topicId, User user) {
        return courseRepository.get(courseId)
                .map(course -> course.removeVote(topicId, user.id()))
                .map(courseRepository::save);
    }

    @Override
    public long determineNumberOfLeftVotes(String id, User user) {
        return votesPerUser - courseRepository.get(id).stream()
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
                .count() < votesPerUser;
    }
}
