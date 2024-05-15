package com.voghbum.androidaudioprojectreactive.mapper;

import com.voghbum.androidaudioprojectreactive.data.entity.Author;
import com.voghbum.androidaudioprojectreactive.dto.AuthorDTO;
import org.mapstruct.Mapper;

@Mapper(implementationName = "AuthorMapperImpl", componentModel = "spring")
public interface AuthorMapper {
    Author toAuthor(AuthorDTO authorDTO);
    AuthorDTO toAuthorDTO(Author author);
}
