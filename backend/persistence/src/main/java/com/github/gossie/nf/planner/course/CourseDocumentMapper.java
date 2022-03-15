package com.github.gossie.nf.planner.course;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
class CourseDocumentMapper {

    private final TopicDocumentMapper topicMapper;

    CourseDocumentMapper(TopicDocumentMapper topicMapper) {
        this.topicMapper = topicMapper;
    }

    CourseDocument map(Course course) {
        return new CourseDocument(course.id(), course.name(), new ArrayList<>(course.topics().stream().map(topicMapper::map).toList()));
    }

    Course map(CourseDocument course) {
        return new Course(course.id(), course.name(), new ArrayList<>(course.topics().stream().map(topicMapper::map).toList()));
    }
}
