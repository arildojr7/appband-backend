package dev.arildo.appband.setlist.repository

import dev.arildo.appband.setlist.model.SetList
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SetListRepository : MongoRepository<SetList, String> {
}