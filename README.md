## Table of Contents
1. **[Goal of the project](#Goal-of-the-project)**
2. **[How to use](#How-to-use)**
3. **[How to run on local machine](#How-to-run-on-local-machine)**
4. **[How to contribute](#How-to-contribute)**
    1. **[Request playlist](#Request-playlist)**
    1. **[Develope code](#Develope-code)**


# Goal of the project
The goal is to create a project that generates playlist on Spotify automatically. The generation can be triggered by hand and by scheduling.

# How to use
I run the code on my local machine, so if the playlist is in the [playlists.json](https://github.com/CoralWombat/spotify-playlist-generator/blob/main/src/main/resources/playlists.json) it should update regurally.

If Your playlist is not in the [playlists.json](https://github.com/CoralWombat/spotify-playlist-generator/blob/main/src/main/resources/playlists.json) You can:
- Add Your own by [contributing](#How-to-contribute).
- Run the server on Your local machine.

# How to run on local machine
1. Clone the code
2. Modify the playlist.json for your playlist(s)
3. Add the application.properties to the root folder (sample can be found [here](https://github.com/CoralWombat/spotify-playlist-generator/blob/main/src/test/resources/dev/kristofgonczo/spotify/playlist/generator/SpotifyPlaylistGeneratorApplicationTests.properties))
4. Run the code
5. Authenticate your profile on the [/authenticate](http://localhost:8080/authenticate) endpoint
6. Then the generation will run automatically at midnight
7. If You want to generate immediately, use the [/regeneratePlaylists](http://localhost:8080/regeneratePlaylists) endpoint

# How to contribute
## Request playlist
Contribute by adding or modifying playlist in the [playlists.json](https://github.com/CoralWombat/spotify-playlist-generator/blob/main/src/main/resources/playlists.json).

If You're requesting a new playlist, use the other playlists as template, leave the playlist id empty.

## Develope code
All contributions are welcome. Make changes as You want, or look into the [Issues](https://github.com/CoralWombat/spotify-playlist-generator/issues) tab, if an Issue is assigned to someone, it means it is in progress.
