package com.voghbum.androidaudioprojectreactive.service;

import com.voghbum.androidaudioprojectreactive.data.dal.AudioBookServiceApplicationDal;
import com.voghbum.androidaudioprojectreactive.data.entity.BookFile;
import com.voghbum.androidaudioprojectreactive.dto.BookFileAllDTO;
import com.voghbum.androidaudioprojectreactive.dto.BookFileDTO;
import com.voghbum.androidaudioprojectreactive.dto.BookFileWithBodyDTO;
import com.voghbum.androidaudioprojectreactive.mapper.BookFileMapper;
import com.voghum.lib.service.TTSType;
import com.voghum.lib.service.TextToSpeechAPI;
import com.voghum.lib.service.TextToSpeechFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class BookFileService {
    private final BookFileMapper bookFileMapper;
    private final AudioBookServiceApplicationDal audioBookServiceApplicationDal;
    @Value("${audio.storage.path}")
    private String storagePath;

    public BookFileService(BookFileMapper bookFileMapper, AudioBookServiceApplicationDal audioBookServiceApplicationDal) {
        this.bookFileMapper = bookFileMapper;
        this.audioBookServiceApplicationDal = audioBookServiceApplicationDal;
    }

    //todo challenge
    public Mono<BookFileDTO> saveBookFile(BookFileWithBodyDTO bookFileDTO) {
        return audioBookServiceApplicationDal.findBookMetadataByName(bookFileDTO.getName())
                .switchIfEmpty(Mono.error(new RuntimeException("BookMetadata not found with name: " + bookFileDTO.getName())))
                .flatMap(bookMetadata -> {
                    return Mono.just(bookFileDTO)
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(bdto -> {
                                TextToSpeechAPI api = new TextToSpeechFactory().create(TTSType.OFFLINE);
                                return Mono.fromCallable(() -> api.get(bdto.getBody()))
                                        .subscribeOn(Schedulers.boundedElastic());
                            })
                            .flatMap(audio -> {
                                String fileName = UUID.randomUUID().toString() + ".mp3";
                                Path filePath = Paths.get(storagePath, fileName);

                                try {
                                    Files.createDirectories(filePath.getParent());
                                    Files.write(filePath, audio);
                                } catch (IOException e) {
                                    return Mono.error(new RuntimeException("Failed to save audio file", e));
                                }

                                BookFile bookFile = new BookFile();
                                bookFile.setAudioPath(filePath.toString());
                                bookFile.setBody(bookFileDTO.getBody());
                                bookFile.setLanguage(bookFileDTO.getLanguage());
                                bookFile.setBookMetadataId(bookMetadata.getId());

                                return audioBookServiceApplicationDal.saveBookFile(bookFile)
                                        .flatMap(savedBookFile -> {
                                            BookFileDTO savedBookFileDTO = bookFileMapper.toBookFileDTO(savedBookFile);
                                            savedBookFileDTO.setName(bookMetadata.getName());
                                            return Mono.just(savedBookFileDTO);
                                        });
                            });
                });
    }

    public Flux<BookFileDTO> findAllBookFiles() {
        return audioBookServiceApplicationDal.findAllBookFiles()
                .flatMap(bookFile -> {
                    return audioBookServiceApplicationDal.findBookMetadataById(bookFile.getBookMetadataId())
                            .map(bookMetadata -> {
                                BookFileDTO bookFileDTO = bookFileMapper.toBookFileDTO(bookFile);
                                bookFileDTO.setName(bookMetadata.getName());
                                return bookFileDTO;
                            });
                });
    }

    public Mono<BookFileAllDTO> findBookFileById(UUID id) {
        Mono<BookFile> persisted = audioBookServiceApplicationDal.findBookFileById(id)
                .switchIfEmpty(Mono.error(new FileNotFoundException("BookFile cannot be found")));

        return persisted.flatMap(bookFile -> {
            return audioBookServiceApplicationDal.findBookMetadataById(bookFile.getBookMetadataId())
                    .switchIfEmpty(Mono.error(new FileNotFoundException("BookMetadata cannot be found")))
                    .map(bookMetadata -> {
                        BookFileAllDTO dto = bookFileMapper.toBookFileAllDTO(bookFile);
                        dto.setName(bookMetadata.getName());
                        return dto;
                    });
        });

    }
}
