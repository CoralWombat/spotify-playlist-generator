package dev.kristofgonczo.spotify.playlist.generator.typed.playlist.generator;

import dev.kristofgonczo.spotify.playlist.generator.properties.Playlist;

public interface PlaylistGenerator {

    default String[] getArtistIds(Playlist playlist) {
        switch (playlist.getArtistListType()) {
            case IDS:
                return playlist.getArtists();
            case NAMES:
                return new String[0];
            default:
                return new String[0];
        }
    }

    void generatePlaylist(Playlist playlist);

}
