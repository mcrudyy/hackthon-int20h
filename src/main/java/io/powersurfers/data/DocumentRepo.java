package io.powersurfers.data;

import io.powersurfers.model.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepo extends MongoRepository<Document, String> {
    Optional<Document> findById(String id);
}
