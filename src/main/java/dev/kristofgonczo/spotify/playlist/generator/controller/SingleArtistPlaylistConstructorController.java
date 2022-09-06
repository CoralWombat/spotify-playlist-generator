package dev.kristofgonczo.spotify.playlist.generator.controller;

import dev.kristofgonczo.spotify.playlist.generator.dtos.GeneratorPlaylist;
import dev.kristofgonczo.spotify.playlist.generator.exception.ArtistNotFoundException;
import dev.kristofgonczo.spotify.playlist.generator.util.RandomCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.Arrays;
import java.util.LinkedList;

@Component
public class SingleArtistPlaylistConstructorController {

    SpotifyAPIController spotifyAPIController;

    @Value("${tracks.per.playlist}")
    private Integer tracksPerPlaylist;

    @Autowired
    public SingleArtistPlaylistConstructorController(SpotifyAPIController spotifyAPIController) {
        this.spotifyAPIController = spotifyAPIController;
    }

    public GeneratorPlaylist generatePlaylist(String[] artists) throws ArtistNotFoundException {
        RandomCollection<Track> topTracks = new RandomCollection<>();

        Artist artist = spotifyAPIController.getArtistById(artists[0]);
        topTracks.addAll(getAllTracksOfArtist(artist));

        LinkedList<Track> finalTracks = new LinkedList<>();
        int i = 0;
        while (!topTracks.isEmpty() && i < tracksPerPlaylist) {
            Track nextTrack = topTracks.next();
            finalTracks.add(nextTrack);
            topTracks.remove(nextTrack);
            i++;
        }

        GeneratorPlaylist result = new GeneratorPlaylist();
        result.setUris(
                finalTracks.stream()
                        .map(track -> track.getUri())
                        .toArray(String[]::new));
        return result;
    }

    private RandomCollection.WeightedItem[] getAllTracksOfArtist(Artist artist) {
        Track[] tracks = spotifyAPIController.getAllTracksOfArtist(artist.getId());
        return Arrays.stream(tracks).map(track -> {
            return new RandomCollection.WeightedItem(
                    getWeight(track),
                    track);
        }).toArray(RandomCollection.WeightedItem[]::new);
    }

    private Integer getWeight(Track track) {
        return track.getPopularity() + 1;
    }
}
