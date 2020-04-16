package com.barbandexplorer.bandfinder.spotify.data.artist;

import java.util.List;

public class ArtistDetailsContainer {

    private List<ArtistDetails> artists;

    public static ArtistDetailsContainer empty() {
        return new ArtistDetailsContainer();
    }

    public List<ArtistDetails> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistDetails> artists) {
        this.artists = artists;
    }
}
