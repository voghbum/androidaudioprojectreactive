package com.voghbum.androidaudioprojectreactive.data.dal;

import com.voghbum.androidaudioprojectreactive.data.entity.Author;
import com.voghbum.androidaudioprojectreactive.data.entity.BookFile;
import com.voghbum.androidaudioprojectreactive.data.entity.BookMetadata;
import com.voghbum.androidaudioprojectreactive.data.repository.AuthorRepository;
import com.voghbum.androidaudioprojectreactive.data.repository.BookFileRepository;
import com.voghbum.androidaudioprojectreactive.data.repository.BookMetadataRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Component
public class AudioBookServiceApplicationDal {
    private final AuthorRepository authorRepository;
    private final BookFileRepository bookFileRepository;
    private final BookMetadataRepository bookMetadataRepository;

    public AudioBookServiceApplicationDal(AuthorRepository authorRepository, BookFileRepository bookFileRepository, 
                                          BookMetadataRepository bookMetadataRepository) {
        this.authorRepository = authorRepository;
        this.bookFileRepository = bookFileRepository;
        this.bookMetadataRepository = bookMetadataRepository;
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
                .filter(Objects::nonNull) // Filter out null author
                .flatMap(author -> {
                    bookMetadata.setAuthor(author.getId());
                    return bookMetadataRepository.save(bookMetadata);
                })
                .onErrorResume(error -> { // Handle errors
                    if (error instanceof NoSuchElementException) {
                        return Mono.error(new NoSuchElementException("Author not found"));
                    } else {
                        return Mono.error(error); // Re-emit other errors
                    }
                });
    }

    public Flux<BookMetadata> searchBookMetadataByBookName(String bookName) {
        return bookMetadataRepository.searchBookMetadataByName(bookName);
    }

    public Flux<BookFile> findAllBookFileByBookMetadataId(UUID bookMetadataId) {
        return bookFileRepository.findAllByBookMetadata(bookMetadataId);
    }

    public Mono<BookFile> saveBookFile(BookFile bookFile) {
        return bookFileRepository.save(bookFile);
    }

    public Flux<BookFile> findAllBookFiles() {
        return bookFileRepository.findAll();
    }

    public Mono<BookFile> findBookFileById(UUID id) {
        return bookFileRepository.findById(id);
    }
}
