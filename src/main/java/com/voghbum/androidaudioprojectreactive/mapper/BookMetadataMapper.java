package com.voghbum.androidaudioprojectreactive.mapper;

import com.voghbum.androidaudioprojectreactive.data.entity.BookMetadata;
import com.voghbum.androidaudioprojectreactive.dto.BookMetadataDTO;
import com.voghbum.androidaudioprojectreactive.dto.BookMetadataRequestDTO;
import com.voghbum.androidaudioprojectreactive.dto.BookMetadataWithAuthorDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "BookMetadataMapperImpl", componentModel = "spring")
public interface BookMetadataMapper {
    //@Mapping(target = "authorName", source = "bookMetadata.author.name")
    BookMetadataDTO toBookMetadataDTO(BookMetadata bookMetadata);
    //@Mapping(target = "author.name", source = "bookMetadataDTO.authorName")
    BookMetadata toBookMetadata(BookMetadataDTO bookMetadataDTO);
    BookMetadataWithAuthorDTO toBookMetadataWithAuthorDTO(BookMetadata bookMetadata);
    //@Mapping(target = "author.name", source = "bookMetadataRequestDTO.authorName")
    BookMetadata toBookMetadata(BookMetadataRequestDTO bookMetadataRequestDTO);
}
