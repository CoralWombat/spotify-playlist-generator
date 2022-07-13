package dev.kristofgonczo.spotify.playlist.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@Configuration
@PropertySource(value = "file:application.properties", ignoreResourceNotFound = true)
@ComponentScan("dev.kristofgonczo.spotify.playlist.generator")
public class SpotifyPlaylistGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyPlaylistGeneratorApplication.class, args);
    }

}
