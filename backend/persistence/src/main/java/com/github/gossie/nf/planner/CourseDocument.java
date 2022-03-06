package com.github.gossie.nf.planner;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
record CourseDocument(@Id String id, String name, List<TopicDocument> topics) {
}
