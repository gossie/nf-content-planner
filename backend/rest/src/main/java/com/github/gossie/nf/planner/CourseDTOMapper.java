package com.github.gossie.nf.planner;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
class CourseDTOMapper {

    private final TopicDTOMapper topicMapper;
    CourseDTOMapper(TopicDTOMapper topicMapper) {
        this.topicMapper = topicMapper;
    }

    Course map(CourseDTO course) {
        return new Course(
                course.id(),
                course.name(),
                course.votes(),
                course.topics() != null ? new ArrayList<>(course.topics().stream().map(topicMapper::map).toList()) : new ArrayList<>()
        );
    }

    CourseDTO map(Course course) {
        return new CourseDTO(
                course.id(),
                course.name(),
                course.votes(), new ArrayList<>(course.topics().stream().map(t -> topicMapper.map(t, course.id())).toList())
        );
    }
}
