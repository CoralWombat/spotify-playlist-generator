package dev.kristofgonczo.spotify.playlist.generator.properties;

import lombok.Data;

@Data
public class Playlist {

    String id;

    PlaylistType playlistType;

    ArtistListType artistListType;

    String[] artists;

}
