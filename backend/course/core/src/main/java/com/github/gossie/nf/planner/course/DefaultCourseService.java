package com.github.gossie.nf.planner.course;

import com.github.gossie.nf.planner.user.User;
import com.github.gossie.nf.planner.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
class DefaultCourseService implements CourseService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCourseService.class);

    private final CourseRepository courseRepository;
    private final UserService userService;
    private final int votesPerUser;

    public DefaultCourseService(CourseRepository courseRepository, UserService userService, @Value("${app.user.max-votes}") int votesPerUser) {
        this.courseRepository = courseRepository;
        this.userService = userService;
        this.votesPerUser = votesPerUser;
    }

    @Override
    public Course createCourse(Course course) {
        User user = userService.findUser(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(IllegalStateException::new);

        Course createdCourse = courseRepository.create(course, user);
        LOG.info("course with id {} was created by user with id {}", createdCourse.id(), user.email());
        return createdCourse;
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
                    LOG.info("topic with id {} was created on course with id {}", createdTopic.id(), courseId);
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
                .map(course -> {
                    var updatedCourse = courseRepository.save(course);
                    LOG.info("topic with id {} war removed from course with id {} was deleted by user with id {}", topicId, courseId, userId);
                    return updatedCourse;
                });
    }

    @Override
    public Optional<Course> deleteCourse(String id, String userId) {
        return courseRepository.get(id)
                .filter(course -> Objects.equals(course.userId(), userId))
                .flatMap(course -> {
                    var deletedCourse = courseRepository.deleteCourse(course.id());
                    LOG.info("course with id {} was deleted by user with id {}", id, userId);
                    return deletedCourse;
                });
    }

    @Override
    public Optional<Course> vote(String courseId, String topicId, User user) {
        return courseRepository.get(courseId)
                .filter(course -> checkIfVotesAreLeft(course, user))
                .map(course -> course.vote(topicId, user.id()))
                .map(course -> {
                    var savedCourse = courseRepository.save(course);
                    LOG.info("vote was added to topic with id {} at course with id {} by user with id {}", topicId, courseId, user.id());
                    return savedCourse;
                });
    }

    @Override
    public Optional<Course> removeVote(String courseId, String topicId, User user) {
        return courseRepository.get(courseId)
                .map(course -> course.removeVote(topicId, user.id()))
                .map(course -> {
                    var savedCourse = courseRepository.save(course);
                    LOG.info("vote was removed from topic with id {} at course with id {} by user with id {}", topicId, courseId, user.id());
                    return savedCourse;
                });
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
