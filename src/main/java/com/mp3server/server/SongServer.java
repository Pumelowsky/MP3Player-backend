package com.mp3server.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ResourceLoader;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;


@SpringBootApplication
public class SongServer {
    public static void main(String[] args) {
        SpringApplication.run(SongServer.class, args);
    }
}

@RestController
class SongController {

    private final ResourceLoader resourceLoader;
    private SongRepository songRepository;

    public SongController(ResourceLoader resourceLoader, SongRepository songRepository) {
        this.resourceLoader = resourceLoader;
        this.songRepository = songRepository;
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<Song> getSongInfo(@PathVariable String id) {
        Optional<Song> song = songRepository.findById(id);
        if (song.isPresent()) {
            return ResponseEntity.ok().body(song.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/play/{filename:.+}")
    public ResponseEntity<Resource> playSong(@PathVariable String filename) {
        Resource resource = loadAsResource(filename);
        if (resource.exists()) {
            return ResponseEntity.ok().body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/allsongs")
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = (List<Song>) songRepository.findAll();
        if (!songs.isEmpty()) {
            return ResponseEntity.ok().body(songs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private Resource loadAsResource(String filename) {
        try {
            return new UrlResource("file:./music/" + filename);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Nie mogłem załadować pliku", e);
        }
    }
}
