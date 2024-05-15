package com.voghbum.androidaudioprojectreactive.dto;

public class BookFileWithAudioDTO {
    private String name;
    private String language;
    private byte[] bookAudio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public byte[] getBookAudio() {
        return bookAudio;
    }

    public void setBookAudio(byte[] bookAudio) {
        this.bookAudio = bookAudio;
    }
}
