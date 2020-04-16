package com.barbandexplorer.bandfinder;

import com.barbandexplorer.bandfinder.spotify.SpotifyClient;
import com.barbandexplorer.bandfinder.spotify.data.artist.Artist;
import com.barbandexplorer.bandfinder.spotify.data.artist.ArtistDetails;
import com.barbandexplorer.bandfinder.spotify.data.artist.ArtistDetailsContainer;
import com.barbandexplorer.bandfinder.spotify.data.playlist.Playlist;
import com.barbandexplorer.bandfinder.spotify.data.playlist.PlaylistItem;
import com.barbandexplorer.bandfinder.spotify.data.playlist.Track;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class TestSpotifyClient {


    @Test
    public void testPlayListTracks() throws IOException, URISyntaxException {

        SpotifyClient spotifyClient = new SpotifyClient();

        Playlist playlist = spotifyClient.getPlaylist("37i9dQZF1DX78toxP7mOaJ");

        Assertions.assertNotEquals(playlist.getItems().size(),0);
        PlaylistItem playlistItem = playlist.getItems().get(0);
        Assertions.assertNotNull(playlistItem.getAddedAt());
        Assertions.assertNotNull(playlistItem.getTrack());
        Track track = playlistItem.getTrack();
        Assertions.assertNotEquals(track.getArtists().size(),0);
        Artist artist = track.getArtists().get(0);
        Assertions.assertNotNull(artist.getHref());
        Assertions.assertNotNull(artist.getId());
        Assertions.assertNotNull(artist.getName());
        Assertions.assertNotEquals(playlist.retrieveAllArtists(),0);
    }

    @Test
    public void testArtists() throws IOException, URISyntaxException {

        SpotifyClient spotifyClient = new SpotifyClient();

        Playlist playlist = spotifyClient.getPlaylist("37i9dQZF1DX78toxP7mOaJ");

        List<Artist> artists = playlist.retrieveAllArtists();

        List<ArtistDetails> artistDetails = spotifyClient.getArtistDetails(artists);

        Assertions.assertNotNull(artistDetails);
        Assertions.assertNotEquals(0,artistDetails.size());
        ArtistDetails example = artistDetails.get(0);
        Assertions.assertNotNull(example.getName());
        Assertions.assertNotNull(example.getExternalUrls());
        Assertions.assertNotNull(example.getFollowers());
        Assertions.assertNotEquals(0,example.getFollowers().getTotal());
        Assertions.assertNotNull(example.getGenres());
        Assertions.assertNotNull(example.getId());
        Assertions.assertNotNull(example.getImages());
        Assertions.assertNotEquals(0,example.getPopularity());
        Assertions.assertNotNull(example.getUri());
    }

    @Test
    public void testRecommendations() throws IOException, URISyntaxException {

        SpotifyClient spotifyClient = new SpotifyClient();

        ArtistDetailsContainer artists = spotifyClient.getRecommendedArtists("5f3pVurmTqsjqOf93rqKno");

        Assertions.assertNotNull(artists);
        Assertions.assertNotEquals(0,artists.getArtists().size());
        ArtistDetails artistDetails = artists.getArtists().get(0);
        Assertions.assertNotNull(artistDetails.getId());
        Assertions.assertNotEquals(0,artistDetails.getFollowers().getTotal());
    }
}
