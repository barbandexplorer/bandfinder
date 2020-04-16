package com.barbandexplorer.bandfinder.spotify.data.playlist;

import com.barbandexplorer.bandfinder.spotify.data.artist.Artist;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {

    private List<Artist> artists;

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
