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
public class MultiArtistPlaylistConstructorController {

    SpotifyAPIController spotifyAPIController;

    @Value("${tracks.per.artist}")
    private Integer tracksPerArtist;

    @Value("${tracks.per.playlist}")
    private Integer tracksPerPlaylist;

    @Autowired
    public MultiArtistPlaylistConstructorController(SpotifyAPIController spotifyAPIController) {
        this.spotifyAPIController = spotifyAPIController;
    }

    public GeneratorPlaylist generatePlaylist(String[] artists) {
        RandomCollection<Track> topTracks = new RandomCollection<>();
        for (String artistId : artists) {
            try {
                Artist artist = spotifyAPIController.getArtistById(artistId);
                topTracks.addAll(getTopNTracks(tracksPerArtist, artist));
            } catch (ArtistNotFoundException e) {
            }
        }

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

    private RandomCollection.WeightedItem[] getTopNTracks(int n, Artist artist) {
        Track[] tracks = spotifyAPIController.getTopNTracksOfArtist(n, artist.getId());
        return Arrays.stream(tracks).limit(n).map(track -> {
            return new RandomCollection.WeightedItem(
                    getWeight(artist, track),
                    track);
        }).toArray(RandomCollection.WeightedItem[]::new);
    }

    private Integer getWeight(Artist artist, Track track) {
        return artist.getFollowers().getTotal() * (track.getPopularity() / 10);
    }

}
