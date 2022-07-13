package dev.kristofgonczo.spotify.playlist.generator.controller;

import com.google.gson.Gson;
import dev.kristofgonczo.spotify.playlist.generator.dtos.GeneratorPlaylist;
import dev.kristofgonczo.spotify.playlist.generator.properties.GeneratedPlaylistsProperties;
import dev.kristofgonczo.spotify.playlist.generator.properties.Playlist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class GeneratorController {

    @Value("classpath:playlists.json")
    Resource resourceFile;

    SpotifyAPIController spotifyAPIController;

    FestivalPlaylistConstructorController festivalPlaylistConstructorController;

    public GeneratorController(SpotifyAPIController spotifyAPIController,
                               FestivalPlaylistConstructorController festivalPlaylistConstructorController) {
        this.spotifyAPIController = spotifyAPIController;
        this.festivalPlaylistConstructorController = festivalPlaylistConstructorController;
    }

    public void generatePlaylists() throws IOException {
        System.out.println("Generation started.");

        GeneratedPlaylistsProperties props =
                new Gson().fromJson(new InputStreamReader(resourceFile.getInputStream()),
                        GeneratedPlaylistsProperties.class);

        for (Playlist playlist : props.getPlaylists()) {
            System.out.println("Generating playlist: " + playlist.getId());

            try {
                GeneratorPlaylist generatePlaylist =
                        festivalPlaylistConstructorController.generatePlaylist(playlist.getArtists());

                spotifyAPIController.wipePlaylist(playlist.getId());

                spotifyAPIController.addItemsToPlaylist(playlist.getId(), generatePlaylist.getUris());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        System.out.println("Generation finished.");
    }

}
