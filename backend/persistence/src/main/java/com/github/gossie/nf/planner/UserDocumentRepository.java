package com.github.gossie.nf.planner;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserDocumentRepository extends MongoRepository<UserDocument, String> {

    Optional<UserDocument> findByUsername(String username);
}
