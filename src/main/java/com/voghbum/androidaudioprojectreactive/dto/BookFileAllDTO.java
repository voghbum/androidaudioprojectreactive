package com.voghbum.androidaudioprojectreactive.dto;

public class BookFileAllDTO {
    private String name;
    private String language;
    private byte[] bookAudio;
    private String body;

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
