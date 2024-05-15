package com.voghbum.androidaudioprojectreactive.controller;

import com.voghbum.androidaudioprojectreactive.dto.AuthorDTO;
import com.voghbum.androidaudioprojectreactive.service.AuthorService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/api/author")
public class AuthorController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorController.class);
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/all")
    public Flux<AuthorDTO> findAllAuthors() {
        return authorService.findAllAuthors()
                            .doOnError(e -> LOG.error("Error retrieving all authors", e));
    }

    @GetMapping("/searchByName")
    public Flux<AuthorDTO> searchAuthorByName(@RequestParam("name") String name) {
        return authorService.searchAuthorByName(name)
                            .doOnError(e -> LOG.error("Error searching author by name: {}", name, e));
    }

    @PostMapping("/save")
    public Mono<AuthorDTO> saveAuthor(@RequestBody AuthorDTO authorDTO) {
        return authorService.saveAuthor(authorDTO)
                            .doOnError(e -> LOG.error("Error saving author", e));
    }
}
