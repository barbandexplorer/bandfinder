package com.barbandexplorer.bandfinder.pipeline;

import com.barbandexplorer.bandfinder.datastore.IDatastore;
import com.barbandexplorer.bandfinder.spotify.data.artist.ArtistDetails;

public class ArtistFilter {

    private IDatastore datastore;

    public ArtistFilter(IDatastore datastore){
        this.datastore = datastore;
    }

    public boolean remove(ArtistDetails artistDetails){

        return artistDetails.getFollowers().getTotal() > 10_000
                || datastore.containsArtist(artistDetails.getId());
    }


}
