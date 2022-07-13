package dev.kristofgonczo.spotify.playlist.generator.controller;

import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    AuthenticatorController authenticatorController;

    SpotifyAPIController spotifyAPIController;

    GeneratorController generatorController;

    @Autowired
    public RestController(
            AuthenticatorController authenticatorController,
            SpotifyAPIController spotifyAPIController,
            GeneratorController generatorController) {
        this.authenticatorController = authenticatorController;
        this.spotifyAPIController = spotifyAPIController;
        this.generatorController = generatorController;
    }

    @GetMapping("/regeneratePlaylists")
    public ResponseEntity<String> regeneratePlaylists() throws IOException {
        generatorController.generatePlaylists();
        return ResponseEntity.ok("Done.");
    }

    @GetMapping("/authenticated")
    public ResponseEntity<String> auth(@RequestParam(required = false) String code) throws IOException, ParseException,
            SpotifyWebApiException {
        authenticatorController.setTokens(code);
        return ResponseEntity.ok("Authenticated.");
    }

    @GetMapping("/authenticate")
    public RedirectView auth() {
        return new RedirectView(authenticatorController.getRedirectUri());
    }

}
