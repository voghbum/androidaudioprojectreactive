package com.voghbum.androidaudioprojectreactive.mapper;

import com.voghbum.androidaudioprojectreactive.data.entity.BookFile;
import com.voghbum.androidaudioprojectreactive.dto.BookFileAllDTO;
import com.voghbum.androidaudioprojectreactive.dto.BookFileDTO;
import com.voghbum.androidaudioprojectreactive.dto.BookFileWithAudioDTO;
import com.voghbum.androidaudioprojectreactive.dto.BookFileWithBodyDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "BookFileMapperImpl", componentModel = "spring")
public interface BookFileMapper {
    //@Mapping(target = "bookMetadata.id", source = "id")
    BookFile toBookFile(BookFileDTO bookFileDTO);

    //@Mapping(target = "bookMetadata", source = "bookMetaData.id")
    BookFileDTO toBookFileDTO(BookFile bookFile);

    //@Mapping(target = "bookMetaData.id", source = "bookMetaData")
    BookFile toBookFile(BookFileWithBodyDTO bookFileDTO);

    //@Mapping(target = "bookMetaData", source = "bookMetaData.id")
    BookFileWithBodyDTO toBookFileWithBodyDTO(BookFile bookFile);

    //@Mapping(target = "bookMetaData.id", source = "bookMetaData")
    BookFile toBookFile(BookFileWithAudioDTO bookFileDTO);

    //@Mapping(target = "bookMetaData", source = "bookMetaData.id")
    BookFileWithAudioDTO toBookFileWithAudioDTO(BookFile bookFile);

    //@Mapping(target = "bookMetaData.id", source = "bookMetaData")
    BookFile toBookFile(BookFileAllDTO bookFileDTO);

    //@Mapping(target = "bookMetaData", source = "bookMetaData.id")
    BookFileAllDTO toBookFileAllDTO(BookFile bookFile);
}
