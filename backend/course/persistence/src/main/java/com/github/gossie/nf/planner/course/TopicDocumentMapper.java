package com.github.gossie.nf.planner.course;

import org.springframework.stereotype.Component;

@Component
class TopicDocumentMapper {

    Topic map(TopicDocument topic) {
        return new Topic(topic.id(), topic.name(), topic.description(), topic.votes());
    }

    TopicDocument map(Topic topic) {
        return new TopicDocument(topic.id(), topic.name(), topic.description(), topic.votes());
    }

}
