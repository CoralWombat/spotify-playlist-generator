package dev.kristofgonczo.spotify.playlist.generator.controller;

import com.google.gson.Gson;
import dev.kristofgonczo.spotify.playlist.generator.dtos.GeneratorPlaylist;
import dev.kristofgonczo.spotify.playlist.generator.properties.GeneratedPlaylistsProperties;
import dev.kristofgonczo.spotify.playlist.generator.properties.Playlist;
import dev.kristofgonczo.spotify.playlist.generator.typed.playlist.generator.MultiArtistPlaylistGenerator;
import dev.kristofgonczo.spotify.playlist.generator.typed.playlist.generator.SingleArtistPlaylistGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class GeneratorController {

    @Value("classpath:playlists.json")
    Resource resourceFile;

    MultiArtistPlaylistGenerator multiArtistPlaylistGenerator;
    SingleArtistPlaylistGenerator singleArtistPlaylistGenerator;

    public GeneratorController(MultiArtistPlaylistGenerator multiArtistPlaylistGenerator,
                               SingleArtistPlaylistGenerator singleArtistPlaylistGenerator) {
        this.multiArtistPlaylistGenerator = multiArtistPlaylistGenerator;
        this.singleArtistPlaylistGenerator = singleArtistPlaylistGenerator;
    }

    @Async
    public void generatePlaylists() throws IOException {
        System.out.println("Generation started.");

        GeneratedPlaylistsProperties props =
                new Gson().fromJson(new InputStreamReader(resourceFile.getInputStream()),
                        GeneratedPlaylistsProperties.class);

        for (Playlist playlist : props.getPlaylists()) {
            switch (playlist.getPlaylistType()) {
                case MULTI_ARTIST:
                    multiArtistPlaylistGenerator.generatePlaylist(playlist);
                    break;
                case SINGLE_ARTIST:
                    singleArtistPlaylistGenerator.generatePlaylist(playlist);
                    break;
            }
        }

        System.out.println("Generation finished.");
    }

}
