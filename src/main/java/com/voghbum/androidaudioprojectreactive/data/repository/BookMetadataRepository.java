package com.voghbum.androidaudioprojectreactive.data.repository;

import com.voghbum.androidaudioprojectreactive.data.entity.BookMetadata;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@Transactional
public interface BookMetadataRepository extends ReactiveCrudRepository<BookMetadata, UUID> {
    Flux<BookMetadata> findAllByAuthorId(UUID authorId);
    @Query("SELECT * FROM book_metadata WHERE LOWER(name) LIKE LOWER(CONCAT(:name, '%'))")
    Flux<BookMetadata> searchBookMetadataByName(String bookName);
    Mono<BookMetadata> findBookMetadataByName(String name);
}
