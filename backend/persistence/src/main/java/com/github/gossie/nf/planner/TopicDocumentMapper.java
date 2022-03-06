package com.github.gossie.nf.planner;

import org.springframework.stereotype.Component;

@Component
public class TopicDocumentMapper {

    public Topic map(TopicDocument topic) {
        return new Topic(topic.id(), topic.name(), topic.description());
    }

    public TopicDocument map(Topic topic) {
        return new TopicDocument(topic.id(), topic.name(), topic.description());
    }

}
