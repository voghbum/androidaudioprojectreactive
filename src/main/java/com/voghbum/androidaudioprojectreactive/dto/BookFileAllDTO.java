package com.voghbum.androidaudioprojectreactive.dto;

public class BookFileAllDTO {
    private String name;
    private String language;
    private String audioPath;
    private String body;

    public String getName() {
        return name;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
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


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
