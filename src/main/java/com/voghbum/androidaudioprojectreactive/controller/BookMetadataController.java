package com.voghbum.androidaudioprojectreactive.controller;

import com.voghbum.androidaudioprojectreactive.dto.BookMetadataDTO;
import com.voghbum.androidaudioprojectreactive.dto.BookMetadataRequestDTO;
import com.voghbum.androidaudioprojectreactive.service.BookMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/api/bookMetaData")
public class BookMetadataController {
    private static final Logger LOG = LoggerFactory.getLogger(BookMetadataController.class);

    private final BookMetadataService bookMetadataService;

    public BookMetadataController(BookMetadataService bookMetadataService) {
        this.bookMetadataService = bookMetadataService;
    }

    @GetMapping("/all")
    public Flux<BookMetadataDTO> findAllBookMetadata() {
        return bookMetadataService.findAllBookMetadata()
                                  .doOnError(e -> LOG.error("Error retrieving all book metadata", e))
                                  .onErrorResume(e -> Flux.empty()); // Return an empty Flux on error
    }

    @GetMapping("/searchByBookName")
    public Flux<BookMetadataDTO> searchBookMetadataByBookName(@RequestParam("name") String name) {
        return bookMetadataService.searchBookMetadataByBookName(name)
                                  .doOnError(e -> LOG.error("Error searching book metadata by book name", e))
                                  .onErrorResume(e -> Flux.empty()); // Return an empty Flux on error
    }

    @PostMapping("/save")
    public Mono<BookMetadataDTO> saveBookMetadata(@RequestBody BookMetadataRequestDTO bookMetadataRequestDTO) {
        return bookMetadataService.saveBookMetadata(bookMetadataRequestDTO)
                                  .doOnError(e -> LOG.error("Error saving book metadata", e))
                                  .onErrorResume(e -> Mono.empty()); // Return an empty Mono on error
    }
}