package com.voghbum.androidaudioprojectreactive.controller;

import com.voghbum.androidaudioprojectreactive.dto.BookFileAllDTO;
import com.voghbum.androidaudioprojectreactive.dto.BookFileDTO;
import com.voghbum.androidaudioprojectreactive.dto.BookFileWithBodyDTO;
import com.voghbum.androidaudioprojectreactive.service.BookFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/bookFile")
public class BookFileController {
    private static final Logger LOG = LoggerFactory.getLogger(BookFileController.class);

    private final BookFileService bookFileService;

    public BookFileController(BookFileService bookFileService) {
        this.bookFileService = bookFileService;
    }

    @PostMapping("/save")
    public Mono<BookFileDTO> saveBookFile(@RequestBody BookFileWithBodyDTO bookFileWithBodyDTO) {
        return bookFileService.saveBookFile(bookFileWithBodyDTO)
                .doOnError(e -> LOG.error("Error saving book file", e))
                .onErrorResume(e -> Mono.empty()); // Optionally return empty Mono or handle differently
    }

    @GetMapping("/all")
    public Flux<BookFileDTO> findAllBookFiles() {
        return bookFileService.findAllBookFiles()
                .doOnError(e -> LOG.error("Error retrieving all book files", e))
                .onErrorResume(e -> Flux.empty()); // Optionally return empty Flux or handle differently
    }

    @GetMapping("/findById")
    public Mono<BookFileAllDTO> findById(@RequestParam("id") UUID id) {
        return bookFileService.findBookFileById(id)
                .doOnError(e -> LOG.error("Error finding book file by ID: {}", id, e))
                .onErrorResume(e -> Mono.empty()); // Optionally return empty Mono or handle differently
    }
}