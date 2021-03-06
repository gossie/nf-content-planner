package com.github.gossie.nf.planner.course;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
record UserDocument(@Id String id, String username, String password) {
}
