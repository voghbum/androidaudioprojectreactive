package com.voghbum.androidaudioprojectreactive.data.repository;

import com.voghbum.androidaudioprojectreactive.data.entity.BookFile;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
@Transactional
public interface BookFileRepository extends ReactiveCrudRepository<BookFile, UUID> {
    Flux<BookFile> findAllByBookMetadataId(UUID bookMetadataId);
}
