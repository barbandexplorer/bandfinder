package com.barbandexplorer.bandfinder.spotify;

public class SpotifySong {
    private String songId;
    private String bandId;


    public SpotifySong(String songId, String bandId) {
        this.songId = songId;
        this.bandId = bandId;
    }

    public String getSongId() {
        return songId;
    }

    public String getBandId() {
        return bandId;
    }
}
