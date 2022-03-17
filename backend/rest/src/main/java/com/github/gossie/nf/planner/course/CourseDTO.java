package com.github.gossie.nf.planner.course;

import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

record CourseDTO(String id, String name, List<TopicDTO> topics, String userId) {

    public List<Link> getLinks() {
        return new ArrayList<>(List.of(
                Link.of("/api/courses/" + id, "self"),
                Link.of("/api/courses/" + id + "/topics", "create-topic"))
        );
    }

}
