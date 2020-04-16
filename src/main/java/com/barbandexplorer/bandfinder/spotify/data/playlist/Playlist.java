package com.barbandexplorer.bandfinder.spotify.data.playlist;

import com.barbandexplorer.bandfinder.spotify.data.artist.Artist;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Playlist {

    private int offset;
    private List<PlaylistItem> items;


    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<PlaylistItem> getItems() {
        return items;
    }

    public void setItems(List<PlaylistItem> items) {
        this.items = items;
    }

    public List<Artist> retrieveAllArtists(){
        Map<String,Artist> artists = new HashMap<>();

        for (PlaylistItem item : items) {
            List<Artist> trackArtists = item.getTrack().getArtists();
            for (Artist trackArtist : trackArtists) {
                if(!artists.containsKey(trackArtist.getId())){
                    artists.put(trackArtist.getId(),trackArtist);
                }
            }
        }

        return new ArrayList<>(artists.values());
    }

    public static Playlist emptyPlaylist(){
        return new Playlist();
    }
}
