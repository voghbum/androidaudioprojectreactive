package com.voghbum.androidaudioprojectreactive.service;

import com.voghbum.androidaudioprojectreactive.data.dal.AudioBookServiceApplicationDal;
import com.voghbum.androidaudioprojectreactive.data.entity.Author;
import com.voghbum.androidaudioprojectreactive.dto.AuthorDTO;
import com.voghbum.androidaudioprojectreactive.mapper.AuthorMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AuthorService {
    private final AudioBookServiceApplicationDal audioBookServiceApplicationDal;
    private final AuthorMapper authorMapper;

    public AuthorService(AudioBookServiceApplicationDal audioBookServiceApplicationDal, AuthorMapper authorMapper) {
        this.audioBookServiceApplicationDal = audioBookServiceApplicationDal;
        this.authorMapper = authorMapper;
    }

    public Mono<AuthorDTO> saveAuthor(AuthorDTO authorDTO) {
        Mono<Author> author = audioBookServiceApplicationDal.saveAuthor(authorMapper.toAuthor(authorDTO));
        return author.map(authorMapper::toAuthorDTO);
    }

    public Flux<AuthorDTO> findAllAuthors() {
        Flux<Author> authors = audioBookServiceApplicationDal.findAllAuthors();
        return authors.map(authorMapper::toAuthorDTO);
    }

    public Flux<AuthorDTO> searchAuthorByName(String name) {
        Flux<Author> authors = audioBookServiceApplicationDal.searchAuthorByName(name);
        return authors.map(authorMapper::toAuthorDTO);
    }
}
