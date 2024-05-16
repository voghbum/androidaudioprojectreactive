package com.voghbum.androidaudioprojectreactive.service;

import com.voghbum.androidaudioprojectreactive.data.dal.AudioBookServiceApplicationDal;
import com.voghbum.androidaudioprojectreactive.data.entity.BookMetadata;
import com.voghbum.androidaudioprojectreactive.dto.BookMetadataDTO;
import com.voghbum.androidaudioprojectreactive.dto.BookMetadataRequestDTO;
import com.voghbum.androidaudioprojectreactive.mapper.BookMetadataMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class BookMetadataService {
    private final AudioBookServiceApplicationDal audioBookServiceApplicationDal;
    private final BookMetadataMapper bookMetadataMapper;

    public BookMetadataService(AudioBookServiceApplicationDal audioBookServiceApplicationDal, BookMetadataMapper bookMetadataMapper) {
        this.audioBookServiceApplicationDal = audioBookServiceApplicationDal;
        this.bookMetadataMapper = bookMetadataMapper;
    }

    public Mono<BookMetadataDTO> saveBookMetadata(BookMetadataRequestDTO bookMetadataRequestDTO) {
        Mono<BookMetadata> bookMetadataMono = audioBookServiceApplicationDal.saveBookMetadata(
                bookMetadataMapper.toBookMetadata(bookMetadataRequestDTO),
                bookMetadataRequestDTO.getAuthorName()
        );

        return bookMetadataMono
                .flatMap(bookMetadata -> {
                    return audioBookServiceApplicationDal.findBookMetadataById(bookMetadata.getAuthorId())
                            .map(author -> {
                                BookMetadataDTO bookMetadataDTO = bookMetadataMapper.toBookMetadataDTO(bookMetadata);
                                bookMetadataDTO.setAuthorName(author.getName());
                                return bookMetadataDTO;
                            });
                });
    }
    public Flux<BookMetadataDTO> findAllBookMetadata() {
        Flux<BookMetadata> bookMetadata = audioBookServiceApplicationDal.findAllBookMetadata();
        return bookMetadata.flatMap(bmd -> audioBookServiceApplicationDal
                .findAuthorById(bmd.getAuthorId())
                .map(a -> {
                    BookMetadataDTO bmDto = bookMetadataMapper.toBookMetadataDTO(bmd);
                    bmDto.setAuthorName(a.getName());
                    return bmDto;
                }));
    }

    public Flux<BookMetadataDTO> searchBookMetadataByBookName(String name) {
        Flux<BookMetadata> books = audioBookServiceApplicationDal.searchBookMetadataByBookName(name);
        return books.flatMap(bmd -> audioBookServiceApplicationDal
                .findAuthorById(bmd.getAuthorId())
                .map(a -> {
                    BookMetadataDTO bmDto = bookMetadataMapper.toBookMetadataDTO(bmd);
                    bmDto.setAuthorName(a.getName());
                    return bmDto;
                }));
    }

}
