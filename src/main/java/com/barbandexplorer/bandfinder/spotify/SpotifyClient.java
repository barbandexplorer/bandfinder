package com.barbandexplorer.bandfinder.spotify;


import com.barbandexplorer.bandfinder.spotify.data.artist.Artist;
import com.barbandexplorer.bandfinder.spotify.data.artist.ArtistDetails;
import com.barbandexplorer.bandfinder.spotify.data.artist.ArtistDetailsContainer;
import com.barbandexplorer.bandfinder.spotify.data.playlist.Playlist;
import com.barbandexplorer.bandfinder.util.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SpotifyClient {

    private static final Log LOG = LogFactory.getLog(SpotifyClient.class);

    private String spotifyUrl;
    private AccessToken accessToken;

    private String clientId;
    private String clientSecret;

    private ObjectMapper objectMapper;

    public SpotifyClient(){
        Configuration configuration = Configuration.getInstance();
        this.spotifyUrl = (String) configuration.getProperty("spotify","url");
        this.clientId = (String) configuration.getProperty("spotify","credentials","client_id");
        this.clientSecret = (String) configuration.getProperty("spotify","credentials","client_secret");

        objectMapper = new ObjectMapper();

    }

    private boolean authenticate() throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost post = new HttpPost(new URIBuilder().setScheme("https").setHost("accounts.spotify.com")
                .setPath("/api/token").build());
        String rawToken = clientId + ":" + clientSecret;
        String token = new String(Base64.getEncoder().encode(rawToken.getBytes(StandardCharsets.ISO_8859_1)));
        post.addHeader("Authorization","Basic " + token);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type","client_credentials"));

        post.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse response = httpClient.execute(post);

        InputStream content = response.getEntity().getContent();

        String json = IOUtils.toString(content, StandardCharsets.UTF_8);

        try {
            accessToken = objectMapper.readValue(json, AccessToken.class);
        }catch (Exception e){
            LOG.error("Exception: ",e);
            return false;
        }

        return true;

    }

    private boolean checkAuthentication() throws IOException, URISyntaxException {
        if(accessToken == null || accessToken.hasExpired()){
            return authenticate();
        }

        return true;
    }

    private HttpGet buildGet(URI uri){
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setHeader("Authorization","Bearer " + accessToken.getAccessToken());
        httpGet.setHeader("Accept","application/json");
        httpGet.setHeader("Content-Type","application/json");
        return httpGet;
    }

    public Playlist getPlaylist(String playListId) throws IOException, URISyntaxException {

        if(!checkAuthentication()){
            return Playlist.emptyPlaylist();
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = buildGet(new URIBuilder().setScheme("https").setHost(spotifyUrl)
                .setPath("v1/playlists/" + playListId + "/tracks").build());

        CloseableHttpResponse response = httpClient.execute(httpGet);

        InputStream content = response.getEntity().getContent();

        String json = IOUtils.toString(content, StandardCharsets.UTF_8);

        return objectMapper.readValue(json,Playlist.class);
    }

    public ArtistDetailsContainer getRecommendedArtists(String id) throws IOException, URISyntaxException {

        if(!checkAuthentication()){
            return ArtistDetailsContainer.empty();
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = buildGet(new URIBuilder().setScheme("https").setHost(spotifyUrl)
            .setPath("v1/artists/" + id + "/related-artists").build());
        CloseableHttpResponse response = httpClient.execute(httpGet);

        String json = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

        return objectMapper.readValue(json,ArtistDetailsContainer.class);
    }

    public List<ArtistDetails> getArtistDetails(List<Artist> artists) throws IOException, URISyntaxException {

        if(!checkAuthentication()){
            return Collections.emptyList();
        }

        List<ArtistBatch> artistBatches = intoBatches(artists);

        List<ArtistDetails> artistDetails = new ArrayList<>();

        for (ArtistBatch artistBatch : artistBatches) {
            artistDetails.addAll(getArtistDetailsBatch(artistBatch).getArtists());
        }

        return artistDetails;
    }

    private ArtistDetailsContainer getArtistDetailsBatch(ArtistBatch artistBatch) throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = buildGet(new URIBuilder().setScheme("https").setHost("api.spotify.com")
                .setPath("/v1/artists").addParameter("ids",artistBatch.getIds()).build());

        CloseableHttpResponse response = httpClient.execute(httpGet);

        String json = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);

        return objectMapper.readValue(json, ArtistDetailsContainer.class);
    }

    private List<ArtistBatch> intoBatches(List<Artist> allArtists){

        List<ArtistBatch> artistBatches = new ArrayList<>();

        int startIndex = 0;
        while (startIndex < allArtists.size()){

            artistBatches.add(new ArtistBatch(allArtists.subList(startIndex,Math.min(startIndex+50,allArtists.size()))));
            startIndex += 50;
        }

        return artistBatches;
    }

    private static class ArtistBatch{
        private List<Artist> artists;

        public ArtistBatch(List<Artist> artists) {
            this.artists = artists;
        }

        public String getIds() {
            return artists.stream().map(Artist::getId).collect(Collectors.joining(","));
        }
    }



}
