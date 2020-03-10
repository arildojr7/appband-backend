package dev.arildo.appband.musician.repository

import dev.arildo.appband.musician.model.Musician
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MusicianRepository : MongoRepository<Musician, String>