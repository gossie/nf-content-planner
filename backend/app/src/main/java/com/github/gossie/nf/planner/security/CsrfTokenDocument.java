package com.github.gossie.nf.planner.security;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("csrfTokens")
public record CsrfTokenDocument(@Id String id, String csrfToken) {
}
