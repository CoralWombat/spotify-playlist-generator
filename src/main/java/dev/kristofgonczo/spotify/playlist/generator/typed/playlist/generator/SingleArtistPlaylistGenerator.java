package dev.kristofgonczo.spotify.playlist.generator.typed.playlist.generator;

import dev.kristofgonczo.spotify.playlist.generator.controller.SingleArtistPlaylistConstructorController;
import dev.kristofgonczo.spotify.playlist.generator.controller.SpotifyAPIController;
import dev.kristofgonczo.spotify.playlist.generator.dtos.GeneratorPlaylist;
import dev.kristofgonczo.spotify.playlist.generator.properties.Playlist;
import org.springframework.stereotype.Component;

@Component
public class SingleArtistPlaylistGenerator implements PlaylistGenerator {

    SpotifyAPIController spotifyAPIController;

    SingleArtistPlaylistConstructorController singleArtistPlaylistConstructorController;

    public SingleArtistPlaylistGenerator(SpotifyAPIController spotifyAPIController,
                                         SingleArtistPlaylistConstructorController singleArtistPlaylistConstructorController) {
        this.spotifyAPIController = spotifyAPIController;
        this.singleArtistPlaylistConstructorController = singleArtistPlaylistConstructorController;
    }

    @Override
    public void generatePlaylist(Playlist playlist) {
        System.out.println("Generating playlist: " + playlist.getId());

        try {
            GeneratorPlaylist generatePlaylist =
                    singleArtistPlaylistConstructorController.generatePlaylist(getArtistIds(playlist));

            spotifyAPIController.wipePlaylist(playlist.getId());

            spotifyAPIController.addItemsToPlaylist(playlist.getId(), generatePlaylist.getUris());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
