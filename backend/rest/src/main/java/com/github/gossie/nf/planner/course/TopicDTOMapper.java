package com.github.gossie.nf.planner.course;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
class TopicDTOMapper {

    Topic map(TopicDTO topic) {
        return new Topic(topic.id(), topic.name(), topic.description(), new ArrayList<>());
    }

    TopicDTO map(Topic topic, String courseId) {
        int votes = topic.votes() == null ? 0 : topic.votes().size();
        return new TopicDTO(topic.id(), topic.name(), topic.description(), votes, courseId);
    }

}
