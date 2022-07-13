package dev.kristofgonczo.spotify.playlist.generator.exception;

public class ArtistNotFoundException extends Exception {
    public ArtistNotFoundException(String artistName) {
        super("Could not find artist: " + artistName);
    }
}
