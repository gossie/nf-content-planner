package com.github.gossie.nf.planner;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDocumentRepository extends MongoRepository<UserDocument, String> {
}
