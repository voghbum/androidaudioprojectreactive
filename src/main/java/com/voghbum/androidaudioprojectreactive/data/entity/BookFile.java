package com.voghbum.androidaudioprojectreactive.data.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.UUID;

@Table(name = "book_file")
public class BookFile implements Serializable, Persistable<UUID> {
    @Id
    private UUID id;
    @Column("metadata_id")
    private UUID bookMetadataId;
    private byte[] bookAudio;
    private String body;
    private String language;
    @Transient
    private boolean isUpdated = false;

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBookMetadataId() {
        return bookMetadataId;
    }

    public void setBookMetadataId(UUID bookMetadataId) {
        this.bookMetadataId = bookMetadataId;
    }

    public byte[] getBookAudio() {
        return bookAudio;
    }

    public void setBookAudio(byte[] bookAudio) {
        this.bookAudio = bookAudio;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    @Override
    public boolean isNew() {
        return this.isUpdated || id == null;
    }
}
