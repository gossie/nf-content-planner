package com.github.gossie.nf.planner.course;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;

@Component
class TopicDTOMapper {

    Topic map(TopicDTO topic) {
        return new Topic(topic.id(), topic.name(), topic.description(), new ArrayList<>());
    }

    TopicDTO map(Topic topic, String courseId, String userId) {
        int allVotes = topic.votes() == null ? 0 : topic.votes().size();
        long userVotes = topic.votes() == null ? 0 : topic.votes().stream().filter(id -> Objects.equals(id, userId)).count();
        return new TopicDTO(topic.id(), topic.name(), topic.description(), allVotes, userVotes, courseId);
    }

}
