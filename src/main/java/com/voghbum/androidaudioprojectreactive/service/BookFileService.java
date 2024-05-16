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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.awt.print.Book;
import java.io.FileNotFoundException;
import java.util.UUID;


@Service
public class BookFileService {
    private final BookFileMapper bookFileMapper;
    private final AudioBookServiceApplicationDal audioBookServiceApplicationDal;

    public BookFileService(BookFileMapper bookFileMapper, AudioBookServiceApplicationDal audioBookServiceApplicationDal) {
        this.bookFileMapper = bookFileMapper;
        this.audioBookServiceApplicationDal = audioBookServiceApplicationDal;
    }

    //todo challenge

    //todo challenge
    public Mono<BookFileDTO> saveBookFile(BookFileWithBodyDTO bookFileDTO) {
        return Mono.just(bookFileDTO)
                .subscribeOn(Schedulers.boundedElastic())  // Move computation to a separate thread pool
                .flatMap(bdto -> {
                    TextToSpeechAPI api = new TextToSpeechFactory().create(TTSType.OFFLINE);
                    return Mono.fromCallable(() -> api.get(bdto.getBody()))  // Wrap the blocking call
                            .subscribeOn(Schedulers.boundedElastic());  // Execute the blocking call on a boundedElastic scheduler
                })
                .flatMap(audio -> audioBookServiceApplicationDal.saveBookFileAudio(audio)  // Save audio and get OID
                        .flatMap(audioOID -> {
                            BookFile bookFile = new BookFile();
                            bookFile.setBody(bookFileDTO.getBody());
                            bookFile.setLanguage(bookFileDTO.getLanguage());
                            bookFile.setAudioOID(audioOID);
                            return audioBookServiceApplicationDal.searchBookMetadataByBookName(bookFileDTO.getName())
                                    .next()  // Since only one item is expected
                                    .doOnNext(bookMetadata -> bookFile.setBookMetadataId(bookMetadata.getId()))
                                    .thenReturn(bookFile);  // Return the populated bookFile entity
                        }))
                .flatMap(entity -> {
                    // Save the entity and convert to DTO
                    return audioBookServiceApplicationDal.saveBookFile(entity)
                            .map(bookFileMapper::toBookFileDTO);
                });
    }
//    public Mono<BookFileDTO> saveBookFile(BookFileWithBodyDTO bookFileDTO) {
//        Mono<BookFileWithBodyDTO> requestMono = Mono.just(bookFileDTO);
//       return requestMono.flatMap(bdto -> {
//            TextToSpeechAPI api = new TextToSpeechFactory().create(TTSType.OFFLINE);
//            byte[] audio = api.get(bdto.getBody());
//            var bookFileAll = new BookFileAllDTO();
//            bookFileAll.setBody(bdto.getBody());
//            bookFileAll.setLanguage(bdto.getLanguage());
//            bookFileAll.setBookAudio(audio);
//            bookFileAll.setName(bdto.getName());
//            BookFile entity = bookFileMapper.toBookFile(bookFileAll);
//            Flux<BookMetadata> bookMetadataFlux = audioBookServiceApplicationDal.searchBookMetadataByBookName(bookFileAll.getName());
//            Flux.combineLatest(bookMetadataFlux, objects -> {
//                List<UUID> bookMetadatas = Arrays.stream(objects).map(obj -> (UUID) obj).toList();
//                entity.setBookMetadata(bookMetadatas.get(0));
//                return bookFileMapper.toBookFileDTO(entity);
//            });
//
////            entity.setBookMetadata(bookMetadataFlux.blockFirst().getId());
////            var persisted =  audioBookServiceApplicationDal.saveBookFile(entity);
////            return persisted.flatMap(p -> bookFileMapper.toBookFileDTO(p));
//        });
//    }

    public Flux<BookFileDTO> findAllBookFiles() {
        Flux<BookFile> files = audioBookServiceApplicationDal.findAllBookFiles();
        return files.map(bookFileMapper::toBookFileDTO);
    }

    public Mono<BookFileAllDTO> findBookFileById(UUID id) {
        Mono<BookFile> persisted = audioBookServiceApplicationDal.findBookFileById(id).switchIfEmpty(Mono.error(new FileNotFoundException("BookFile cannot found")));
        return persisted.map(bookFileMapper::toBookFileAllDTO);
    }
}
