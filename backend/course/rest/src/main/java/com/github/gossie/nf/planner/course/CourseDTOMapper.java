package com.github.gossie.nf.planner.course;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;

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
                course.topics() != null ? new ArrayList<>(course.topics().stream().map(topicMapper::map).toList()) : new ArrayList<>(),
                course.userId()
        );
    }

    CourseDTO map(Course course, String userId) {
        return new CourseDTO(
                course.id(),
                course.name(),
                new ArrayList<>(course.topics().stream().map(t -> topicMapper.map(t, course.id(), userId)).sorted(Comparator.comparingInt(TopicDTO::allVotes).reversed()).toList()),
                course.userId()
        );
    }
}
