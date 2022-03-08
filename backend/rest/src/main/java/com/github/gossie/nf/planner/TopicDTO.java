package com.github.gossie.nf.planner;

import net.minidev.json.annotate.JsonIgnore;
import org.springframework.hateoas.Link;

import java.util.List;

public record TopicDTO(String id, String name, String description, @JsonIgnore String courseId) {

    public List<Link> links() {
        return List.of(Link.of("/api/courses/" + courseId + "/topics/" + id, "self"));
    }

}
