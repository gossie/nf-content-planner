package com.github.gossie.nf.planner;

import org.springframework.stereotype.Component;

@Component
public class TopicDTOMapper {

    public Topic map(TopicDTO topic) {
        return new Topic(null, topic.name(), topic.description());
    }

    public TopicDTO map(Topic topic) {
        return new TopicDTO(topic.name(), topic.description());
    }

}
