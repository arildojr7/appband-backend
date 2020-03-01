package dev.arildo.appband.song.repository

import dev.arildo.appband.song.model.Song
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SongRepository : MongoRepository<Song, String> {
}