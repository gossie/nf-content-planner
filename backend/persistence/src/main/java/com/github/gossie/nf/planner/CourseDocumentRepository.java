package com.github.gossie.nf.planner;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CourseDocumentRepository extends MongoRepository<CourseDocument, String> {
}
