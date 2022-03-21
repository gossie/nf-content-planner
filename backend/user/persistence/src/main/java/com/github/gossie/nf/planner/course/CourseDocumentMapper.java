package com.github.gossie.nf.planner.course;

import com.github.gossie.nf.planner.user.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
class CourseDocumentMapper {

    private final TopicDocumentMapper topicMapper;

    CourseDocumentMapper(TopicDocumentMapper topicMapper) {
        this.topicMapper = topicMapper;
    }

    CourseDocument map(Course course, User user) {
        return new CourseDocument(course.id(), course.name(), new ArrayList<>(course.topics().stream().map(topicMapper::map).toList()), user.id());
    }

    CourseDocument map(Course course) {
        // TODO das null am ende
        return new CourseDocument(course.id(), course.name(), new ArrayList<>(course.topics().stream().map(topicMapper::map).toList()), course.userId());
    }

    Course map(CourseDocument course) {
        return new Course(course.id(), course.name(), new ArrayList<>(course.topics().stream().map(topicMapper::map).toList()), course.userId());
    }
}
