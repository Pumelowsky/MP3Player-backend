package com.mp3server.server;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SongRepository extends CrudRepository<Song, String> {
    Optional<Song> findById(String id);
}