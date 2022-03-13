package com.github.gossie.nf.planner;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
class CourseDocumentMapper {

    private final TopicDocumentMapper topicMapper;

    public CourseDocumentMapper(TopicDocumentMapper topicMapper) {
        this.topicMapper = topicMapper;
    }

    public CourseDocument map(Course course) {
        return new CourseDocument(course.id(), course.name(), course.votes(), new ArrayList<>(course.topics().stream().map(topicMapper::map).toList()));
    }

    public Course map(CourseDocument course) {
        return new Course(course.id(), course.name(), course.votes(), new ArrayList<>(course.topics().stream().map(topicMapper::map).toList()));
    }
}
