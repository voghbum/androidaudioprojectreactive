package com.voghbum.androidaudioprojectreactive.data.dal;

import com.voghbum.androidaudioprojectreactive.data.entity.Author;
import com.voghbum.androidaudioprojectreactive.data.entity.BookFile;
import com.voghbum.androidaudioprojectreactive.data.entity.BookMetadata;
import com.voghbum.androidaudioprojectreactive.data.repository.AuthorRepository;
import com.voghbum.androidaudioprojectreactive.data.repository.BookFileRepository;
import com.voghbum.androidaudioprojectreactive.data.repository.BookMetadataRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.print.Book;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
public class AudioBookServiceApplicationDal {
    private final AuthorRepository authorRepository;
    private final BookFileRepository bookFileRepository;
    private final BookMetadataRepository bookMetadataRepository;
    private final DatabaseClient dbClient;

    public AudioBookServiceApplicationDal(AuthorRepository authorRepository, BookFileRepository bookFileRepository,
                                          BookMetadataRepository bookMetadataRepository, DatabaseClient dbClient) {
        this.authorRepository = authorRepository;
        this.bookFileRepository = bookFileRepository;
        this.bookMetadataRepository = bookMetadataRepository;
        this.dbClient = dbClient;
    }

    public Flux<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    public Mono<Author> saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Flux<Author> searchAuthorByName(String name) {
        return authorRepository.searchAuthorByName(name);
    }

    public Flux<BookMetadata> findAllBookMetadata() {
        return bookMetadataRepository.findAll();
    }

    public Flux<BookMetadata> findAllBookMetadataByAuthorId(UUID authorId) {
        return bookMetadataRepository.findAllByAuthorId(authorId);
    }

    public Mono<BookMetadata> saveBookMetadata(BookMetadata bookMetadata, String authorName) {
        return authorRepository.findAuthorByName(authorName)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Author not found"))) // Handle author not found
                .flatMap(author -> {
                    BookMetadata updatedMetadata = new BookMetadata(bookMetadata); // Clone or create new instance if mutable
                    updatedMetadata.setAuthorId(author.getId());
                    return bookMetadataRepository.save(updatedMetadata);
                })
                .onErrorResume(error -> { // Handle errors more generically if needed
                    if (error instanceof NoSuchElementException) {
                        return Mono.error(new NoSuchElementException("Author not found"));
                    } else {
                        return Mono.error(error); // Propagate other errors
                    }
                });
    }
    public Flux<BookMetadata> searchBookMetadataByBookName(String bookName) {
        return bookMetadataRepository.searchBookMetadataByName(bookName);
    }

    public Flux<BookFile> findAllBookFileByBookMetadataId(UUID bookMetadataId) {
        return bookFileRepository.findAllByBookMetadataId(bookMetadataId);
    }

    public Mono<BookFile> saveBookFile(BookFile bookFile) {
        return bookFileRepository.save(bookFile);
    }

    public Mono<Long> saveBookFileAudio(byte[] audio) {
        return createLargeObject(audio);
    }

    private Mono<Long> createLargeObject(byte[] data) {
        return dbClient.sql("SELECT lo_create(0)")
                .fetch()
                .first()
                .map(result -> (Long) result.get("lo_create"))
                .flatMap(oid -> dbClient.sql("SELECT lo_open(:oid, 131072)") // 131072 is INV_WRITE
                        .bind("oid", oid)
                        .fetch()
                        .rowsUpdated()
                        .then(Mono.just(oid))
                        .flatMap(o -> dbClient.sql("SELECT lowrite(lo_open(:oid, 131072), :data)")
                                .bind("oid", o)
                                .bind("data", data)
                                .fetch()
                                .rowsUpdated()
                                .then(Mono.just(o))
                        ));
    }

    public Flux<BookFile> findAllBookFiles() {
        return bookFileRepository.findAll();
    }

    public Mono<BookFile> findBookFileById(UUID id) {
        return bookFileRepository.findById(id);
    }

    public Mono<Author> findAuthorById(UUID authorId) {
        return authorRepository.findById(authorId);
    }
}
