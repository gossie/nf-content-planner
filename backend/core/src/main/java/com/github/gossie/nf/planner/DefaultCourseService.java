package com.github.gossie.nf.planner;

import org.springframework.stereotype.Service;

import java.util.List;
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
}
