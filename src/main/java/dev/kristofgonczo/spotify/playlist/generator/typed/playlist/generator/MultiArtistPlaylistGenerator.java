package dev.kristofgonczo.spotify.playlist.generator.typed.playlist.generator;

import dev.kristofgonczo.spotify.playlist.generator.controller.MultiArtistPlaylistConstructorController;
import dev.kristofgonczo.spotify.playlist.generator.controller.SpotifyAPIController;
import dev.kristofgonczo.spotify.playlist.generator.dtos.GeneratorPlaylist;
import dev.kristofgonczo.spotify.playlist.generator.properties.Playlist;
import org.springframework.stereotype.Component;

@Component
public class MultiArtistPlaylistGenerator implements PlaylistGenerator {

    SpotifyAPIController spotifyAPIController;

    MultiArtistPlaylistConstructorController multiArtistPlaylistConstructorController;

    public MultiArtistPlaylistGenerator(SpotifyAPIController spotifyAPIController,
                                        MultiArtistPlaylistConstructorController multiArtistPlaylistConstructorController) {
        this.spotifyAPIController = spotifyAPIController;
        this.multiArtistPlaylistConstructorController = multiArtistPlaylistConstructorController;
    }

    @Override
    public void generatePlaylist(Playlist playlist) {
        System.out.println("Generating playlist: " + playlist.getId());

        try {
            GeneratorPlaylist generatePlaylist =
                    multiArtistPlaylistConstructorController.generatePlaylist(getArtistIds(playlist));

            spotifyAPIController.wipePlaylist(playlist.getId());

            spotifyAPIController.addItemsToPlaylist(playlist.getId(), generatePlaylist.getUris());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
