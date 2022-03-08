package com.github.gossie.nf.planner;

import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

record CourseDTO(String id, String name, List<TopicDTO> topics) {

    public List<Link> getLinks() {
        return new ArrayList<>(List.of(
                Link.of("/api/courses/" + id, "self"),
                Link.of("/api/courses/" + id + "/topics", "create-topic"))
        );
    }

}
