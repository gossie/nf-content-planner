package com.github.gossie.nf.planner.course;

import java.util.List;

public record TopicDocument(String id, String name, String description, List<String> votes) {
}
