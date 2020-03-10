package dev.arildo.appband.song.repository

import com.fasterxml.jackson.databind.ObjectMapper
import dev.arildo.appband.song.service.dto.SongResponseDTO
import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate

class SongCustomRepositoryImpl(private val mongoTemplate: MongoTemplate,
                               private val objectMapper: ObjectMapper) : SongCustomRepository {
    override fun getSongs(): List<SongResponseDTO> {
        val resultList = mutableListOf<SongResponseDTO>()
        val collection = mongoTemplate.getCollection(SONG_COLLECTION)
        val pipeline = listOf(
                Document()
                        .append("\$lookup", Document()
                                .append("from", "musician")
                                .append("localField", "singerId")
                                .append("foreignField", "_id")
                                .append("as", "singer")
                        ),
                Document()
                        .append("\$unwind", "\$singer")
        )

        collection.aggregate(pipeline).allowDiskUse(true).forEach {
            objectMapper.readValue(it.toJson(), SongResponseDTO::class.java).apply {
                resultList.add(copy(chord = chord))
            }
        }
        return resultList
    }

    companion object {

        const val SONG_COLLECTION = "song"
    }
}