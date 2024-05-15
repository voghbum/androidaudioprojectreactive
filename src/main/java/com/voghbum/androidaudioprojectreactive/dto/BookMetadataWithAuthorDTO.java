package com.voghbum.androidaudioprojectreactive.dto;


import java.time.LocalDateTime;
import java.util.UUID;

public class BookMetadataWithAuthorDTO {
    private UUID id;
    private String name;
    private String description;
    private String writtenYear;
    private LocalDateTime insertDateTime;
    public AuthorDTO author;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWrittenYear() {
        return writtenYear;
    }

    public void setWrittenYear(String writtenYear) {
        this.writtenYear = writtenYear;
    }

    public LocalDateTime getInsertDateTime() {
        return insertDateTime;
    }

    public void setInsertDateTime(LocalDateTime insertDateTime) {
        this.insertDateTime = insertDateTime;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }
}
