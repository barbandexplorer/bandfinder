package com.barbandexplorer.bandfinder.pipeline;

import com.barbandexplorer.bandfinder.datastore.IDatastore;
import com.barbandexplorer.bandfinder.datastore.TestDataStore;
import com.barbandexplorer.bandfinder.spotify.SpotifyClient;
import com.barbandexplorer.bandfinder.spotify.data.artist.Artist;
import com.barbandexplorer.bandfinder.spotify.data.artist.ArtistDetails;
import com.barbandexplorer.bandfinder.spotify.data.playlist.Playlist;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Pipeline {

    private SpotifyClient spotifyClient;
    private ArtistFilter artistFilter;
    private IDatastore datastore;

    public Pipeline(){
        spotifyClient = new SpotifyClient();
        datastore = new TestDataStore();
        artistFilter = new ArtistFilter(datastore);
    }

    public void processRequest(String playListId) throws IOException, URISyntaxException {

        Playlist playlist = spotifyClient.getPlaylist(playListId);

        List<Artist> artists = playlist.retrieveAllArtists();

        List<ArtistDetails> artistDetails = spotifyClient.getArtistDetails(artists);

        artistDetails.removeIf(e -> artistFilter.remove(e));



    }



}
