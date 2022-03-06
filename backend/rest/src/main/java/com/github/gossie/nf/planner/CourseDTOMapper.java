package com.github.gossie.nf.planner;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
class CourseDTOMapper {

    private final TopicDTOMapper topicMapper;
    CourseDTOMapper(TopicDTOMapper topicMapper) {
        this.topicMapper = topicMapper;
    }

    public Course map(CourseDTO course) {
        return new Course(null, course.name(), new ArrayList<>(course.topics().stream().map(topicMapper::map).toList()));
    }

    public CourseDTO map(Course course) {
        return new CourseDTO(course.name(), new ArrayList<>(course.topics().stream().map(topicMapper::map).toList()));
    }
}
