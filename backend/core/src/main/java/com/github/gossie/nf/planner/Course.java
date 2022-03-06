package com.github.gossie.nf.planner;

import java.util.List;
import java.util.UUID;

public record Course(String id, String name, List<Topic> topics) {
    public Topic addTopic(Topic topic) {
        Topic newTopic = new Topic(UUID.randomUUID().toString(), topic.name(), topic.description());
        topics.add(newTopic);
        return newTopic;
    }
}
