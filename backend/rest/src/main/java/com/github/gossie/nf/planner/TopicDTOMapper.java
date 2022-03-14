package com.github.gossie.nf.planner;

import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
class TopicDTOMapper {

    Topic map(TopicDTO topic) {
        return new Topic(topic.id(), topic.name(), topic.description(), new HashSet<>());
    }

    TopicDTO map(Topic topic, String courseId) {
        return new TopicDTO(topic.id(), topic.name(), topic.description(), topic.votes().size(), courseId);
    }

}
