package com.barbandexplorer.bandfinder.datastore;

public interface IDatastore {

    boolean containsArtist(String artistId);
    void saveArtist(FinalArtist finalArtist);
}
