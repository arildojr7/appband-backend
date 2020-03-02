package dev.arildo.appband.singer.repository

import dev.arildo.appband.singer.model.Singer
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SingerRepository : MongoRepository<Singer, String>