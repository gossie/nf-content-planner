package com.github.gossie.nf.planner.course;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "courses")
record CourseDocument(@Id String id, String name, List<TopicDocument> topics, String userId) {
}
