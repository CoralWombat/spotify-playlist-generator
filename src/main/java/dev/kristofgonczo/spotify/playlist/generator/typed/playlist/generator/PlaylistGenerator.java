package dev.kristofgonczo.spotify.playlist.generator.typed.playlist.generator;

import com.google.gson.Gson;
import dev.kristofgonczo.spotify.playlist.generator.properties.Playlist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public interface PlaylistGenerator {

    @Value("classpath:artist_id_dictionary.json")
    Resource dictionaryFile = null;

    default String[] getArtistIds(Playlist playlist) throws IOException {
        switch (playlist.getArtistListType()) {
            case IDS:
                return playlist.getArtists();
            case NAMES:
                return getArtistIdsByArtistNames(playlist.getArtists());
            default:
                return getArtistIdsByArtistNames(playlist.getArtists());
        }
    }

    private String[] getArtistIdsByArtistNames(String[] artistNames) {
        Map<String, String> dictionary =
                new Gson().fromJson(new InputStreamReader(getClass().getClassLoader()
                                .getResourceAsStream("artist_id_dictionary.json")),
                        Map.class);

        List<String> artistIds = new LinkedList<>();

        for (String artistName : artistNames) {
            String artistId = dictionary.getOrDefault(artistName, "missing");
            if (artistId != null && !artistId.equals("NotOnSpotify")) {
                if (artistId.equals("missing")) {
                    System.out.println("Artist is missing from dictionary file: " + artistName);
                } else {
                    artistIds.add(artistId);
                }
            }
        }

        return artistIds.toArray(new String[0]);
    }

    void generatePlaylist(Playlist playlist);

}
