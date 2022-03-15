package com.github.gossie.nf.planner.course;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CourseDocumentRepository extends MongoRepository<CourseDocument, String> {
}
