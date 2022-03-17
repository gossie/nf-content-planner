package com.github.gossie.nf.planner.course;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record Course(String id, String name, List<Topic> topics, String userId) {
    public Topic addTopic(Topic topic) {
        Topic newTopic = new Topic(UUID.randomUUID().toString(), topic.name(), topic.description(), topic.votes());
        topics.add(newTopic);
        return newTopic;
    }

    public Course removeTopic(String topicId) {
        topics.removeIf(t -> Objects.equals(t.id(), topicId));
        return this;
    }

    public Course vote(String topicId, String userId) {
        topics.stream()
                .filter(t -> Objects.equals(t.id(), topicId))
                .findFirst()
                .ifPresent(t -> t.votes().add(userId));
        return this;
    }
}
