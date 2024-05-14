package com.voghbum.androidaudioprojectreactive.data.repository;

import com.voghbum.androidaudioprojectreactive.data.entity.BookMetadata;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
@Transactional
public interface BookMetadataRepository extends ReactiveCrudRepository<BookMetadata, UUID> {
    Flux<BookMetadata> findAllByAuthorId(UUID authorId);
    Flux<BookMetadata> searchBookMetadataByName(String bookName);
}
