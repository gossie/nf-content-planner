package com.github.gossie.nf.planner;

import org.springframework.stereotype.Component;

@Component
public class TopicDTOMapper {

    public Topic map(TopicDTO topic) {
        return new Topic(topic.id(), topic.name(), topic.description());
    }

    public TopicDTO map(Topic topic, String courseId) {
        return new TopicDTO(topic.id(), topic.name(), topic.description(), courseId);
    }

}
