package dev.kristofgonczo.spotify.playlist.generator.scheduler;

import dev.kristofgonczo.spotify.playlist.generator.controller.GeneratorController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    GeneratorController generatorController;

    @Autowired
    public Scheduler(GeneratorController generatorController) {
        this.generatorController = generatorController;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void generatePlaylists() {
        try {
            generatorController.generatePlaylists();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
