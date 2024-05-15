package com.voghbum.androidaudioprojectreactive.data.repository;

import com.voghbum.androidaudioprojectreactive.data.entity.Author;
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
public interface AuthorRepository extends ReactiveCrudRepository<Author, UUID> {
    Mono<Author> findAuthorByName(String name);
    @Query("SELECT * FROM author WHERE LOWER(name) LIKE LOWER(CONCAT(:name, '%'))")
    Flux<Author> searchAuthorByName(String name);
}
