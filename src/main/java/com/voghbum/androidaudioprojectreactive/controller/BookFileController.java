package com.voghbum.androidaudioprojectreactive.controller;

import com.voghbum.androidaudioprojectreactive.dto.BookFileAllDTO;
import com.voghbum.androidaudioprojectreactive.dto.BookFileDTO;
import com.voghbum.androidaudioprojectreactive.dto.BookFileWithBodyDTO;
import com.voghbum.androidaudioprojectreactive.service.BookFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import static java.lang.StringTemplate.STR;

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

    @GetMapping("/stream/{id}")
    public Mono<ResponseEntity<Resource>> streamAudio(@PathVariable UUID id,
                                                      @RequestHeader HttpHeaders headers) {
        return bookFileService.findBookFileById(id)
                .switchIfEmpty(Mono.error(new FileNotFoundException("BookFile not found")))
                .flatMap(bookFile -> {
                    Path filePath = Paths.get(bookFile.getAudioPath());
                    if (!Files.exists(filePath)) {
                        return Mono.just(ResponseEntity.notFound().build());
                    }

                    try {
                        Resource resource = new UrlResource(filePath.toUri());
                        HttpRange range = headers.getRange().stream().findFirst().orElse(null);
                        long contentLength = resource.contentLength();

                        if (range == null) {
                            return Mono.just(ResponseEntity.ok()
                                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                                    .body(resource));
                        } else {
                            long start = range.getRangeStart(contentLength);
                            long end = range.getRangeEnd(contentLength);
                            long rangeLength = end - start + 1;

                            SeekableByteChannel byteChannel = Files.newByteChannel(filePath, StandardOpenOption.READ);
                            byteChannel.position(start);
                            InputStreamResource inputStreamResource = new InputStreamResource(Channels.newInputStream(byteChannel));

                            return Mono.just(ResponseEntity.status(206)
                                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(rangeLength))
                                    .header(HttpHeaders.CONTENT_RANGE, STR."bytes \{start}-\{end}/\{contentLength}")
                                    .body(inputStreamResource));
                        }
                    } catch (IOException e) {
                        return Mono.just(ResponseEntity.status(500).build());
                    }
                });
    }


}