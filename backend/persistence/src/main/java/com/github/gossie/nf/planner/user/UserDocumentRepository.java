package com.github.gossie.nf.planner.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserDocumentRepository extends MongoRepository<UserDocument, String> {
}
