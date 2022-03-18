package com.github.gossie.nf.planner.course;

import net.minidev.json.annotate.JsonIgnore;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

public record TopicDTO(String id, String name, String description, int votes, long userVotes, @JsonIgnore String courseId) {

    public List<Link> getLinks() {
        return new ArrayList<>(List.of(
                Link.of("/api/courses/" + courseId + "/topics/" + id, "self"),
                Link.of("/api/courses/" + courseId + "/topics/" + id + "/votes", "vote")
        ));
    }

}
