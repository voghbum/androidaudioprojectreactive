package com.voghbum.androidaudioprojectreactive.data.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "book_metadata")
public class BookMetadata implements Serializable, Persistable<UUID> {
    @Id
    private UUID id;
    @Column("author_id")
    private UUID authorId;
    private String name;
    private String description;
    private String writtenYear;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime insertDateTime = LocalDateTime.now();

    public BookMetadata(BookMetadata bookMetadata) {
        this.setAuthorId(bookMetadata.getAuthorId());
        this.setId(bookMetadata.getId());
        this.setDescription(bookMetadata.getDescription());
        this.setUpdated(bookMetadata.isUpdated());
        this.setWrittenYear(bookMetadata.getWrittenYear());
        this.setName(bookMetadata.getName());
    }

    public BookMetadata() {

    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
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

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    @Transient
    private boolean isUpdated = false;

    @Override
    public boolean isNew() {
        return this.isUpdated || id == null;
    }
}
