package dev.kristofgonczo.spotify.playlist.generator.controller;

import com.google.gson.JsonParser;
import com.neovisionaries.i18n.CountryCode;
import dev.kristofgonczo.spotify.playlist.generator.exception.ArtistNotFoundException;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsTopTracksRequest;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import se.michaelthelin.spotify.requests.data.playlists.RemoveItemsFromPlaylistRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SpotifyAPIController {

    AuthenticatorController authenticatorController;

    @Autowired
    public SpotifyAPIController(AuthenticatorController authenticatorController) {
        this.authenticatorController = authenticatorController;
    }

    private static final Integer QUERY_PLAYLIST_ITEMS_LIMIT = 50;

    private static final Integer ADD_PLAYLIST_ITEMS_LIMIT = 100;

    public void wipePlaylist(String playlistId) {
        try {
            authenticatorController.refreshTokens();

            List<PlaylistTrack> tracks = getPlaylistItems(playlistId);

            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(String.join(",", tracks.stream()
                    .map(playlistTrack -> "{\"uri\":\"" + playlistTrack.getTrack().getUri() + "\"}")
                    .toArray(String[]::new)));
            sb.append("]");

            RemoveItemsFromPlaylistRequest removeItemsFromPlaylistRequest = authenticatorController
                    .getSpotifyApi()
                    .removeItemsFromPlaylist(playlistId, JsonParser.parseString(sb.toString()).getAsJsonArray())
                    .build();

            removeItemsFromPlaylistRequest.execute();
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            e.printStackTrace();
            System.out.println("Could not wipe playlist: " + playlistId);
        }
    }

    public List<PlaylistTrack> getPlaylistItems(String playlistId) {
        ArrayList<PlaylistTrack> result = new ArrayList<>();
        try {
            authenticatorController.refreshTokens();

            boolean allRequested = false;

            while (!allRequested) {
                final GetPlaylistsItemsRequest getPlaylistsItemsRequest = authenticatorController
                        .getSpotifyApi()
                        .getPlaylistsItems(playlistId)
                        .offset(result.size())
                        .limit(QUERY_PLAYLIST_ITEMS_LIMIT)
                        .market(CountryCode.HU)
                        .build();
                Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsItemsRequest.execute();

                result.addAll(Arrays.stream(playlistTrackPaging.getItems()).toList());

                allRequested = result.size() == playlistTrackPaging.getTotal();
            }
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            e.printStackTrace();
            System.out.println("Could not get playlist items: " + playlistId);
        }
        return result;
    }

    public void addItemsToPlaylist(String playlistId, String[] uris) {
        try {
            authenticatorController.refreshTokens();

            Integer i = 0;
            while (i * ADD_PLAYLIST_ITEMS_LIMIT < uris.length) {
                AddItemsToPlaylistRequest addItemsToPlaylistRequest = authenticatorController
                        .getSpotifyApi()
                        .addItemsToPlaylist(playlistId, uris)
                        .build();
                addItemsToPlaylistRequest.execute();

                i++;
            }
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            e.printStackTrace();
            System.out.println("Could not add items to playlist: " + playlistId);
        }
    }

    public Artist getArtistByName(String name) throws ArtistNotFoundException {
        try {
            authenticatorController.refreshTokens();

            SearchArtistsRequest searchArtistsRequest = authenticatorController
                    .getSpotifyApi()
                    .searchArtists(name)
                    .market(CountryCode.HU)
                    .build();
            Paging<Artist> artistPaging = searchArtistsRequest.execute();
            return Arrays.stream(artistPaging.getItems()).findFirst().orElse(null);
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            e.printStackTrace();
            System.out.println("Could not get artist: " + name);
        }
        throw new ArtistNotFoundException(name);
    }

    public Artist getArtistById(String artistId) throws ArtistNotFoundException {
        try {
            authenticatorController.refreshTokens();

            GetArtistRequest getArtistRequest = authenticatorController
                    .getSpotifyApi()
                    .getArtist(artistId)
                    .build();

            Artist artist = getArtistRequest.execute();
            if (artist != null) return artist;
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            e.printStackTrace();
            System.out.println("Could not get artist: " + artistId);
        }
        throw new ArtistNotFoundException(artistId);
    }

    public Track[] getTopNTracksOfArtist(int n, String artistId) {
        Track[] result = new Track[]{};
        try {
            authenticatorController.refreshTokens();

            GetArtistsTopTracksRequest getArtistsTopTracksRequest =
                    authenticatorController
                            .getSpotifyApi()
                            .getArtistsTopTracks(artistId, CountryCode.HU)
                            .build();
            Track[] tracks = getArtistsTopTracksRequest.execute();
            result = Arrays.stream(tracks).limit(n).toArray(Track[]::new);
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            e.printStackTrace();
            System.out.println("Could not get top tracks of artist: " + artistId);
        }
        return result;
    }
}
