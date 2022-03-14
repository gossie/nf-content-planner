package com.github.gossie.nf.planner;

import java.util.Set;

public record TopicDocument(String id, String name, String description, Set<String> votes) {
}
