package com.barbandexplorer.bandfinder.datastore;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TestDataStore implements IDatastore{

    private CSVWriter csvWriter;

    private Set<String> seen;

    public TestDataStore() {
        try {
            csvWriter = new CSVWriter(new FileWriter("testDatastore.tsv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        seen = new HashSet<>();
    }

    @Override
    public boolean containsArtist(String artistId) {
        return seen.contains(artistId);
    }

    @Override
    public void saveArtist(FinalArtist finalArtist) {

        seen.add(finalArtist.getId());

        try {
            csvWriter.writeNext(new String[]{finalArtist.getId(), finalArtist.getGenre(), finalArtist.getName(),
                    finalArtist.getLocationId(), finalArtist.getLocationName(), finalArtist.getSubgenre(), finalArtist.getGenre()});
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
