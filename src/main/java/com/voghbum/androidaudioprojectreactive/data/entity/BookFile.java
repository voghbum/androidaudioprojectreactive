package com.voghbum.androidaudioprojectreactive.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "bookFile")
public class BookFile implements Serializable, Persistable<UUID> {
    @Id
    public UUID id;
    public UUID bookMetadata;
    public byte[] bookAudio;
    public String body;
    public String language;
    @Transient
    private boolean isUpdated = false;

    @Override
    public boolean isNew() {
        return this.isUpdated || id == null;
    }
}
