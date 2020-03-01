package dev.arildo.appband.song.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("song")
class Song(
        @Id
        var id: String? = null,
        var title: String = "",
        var singerId: ObjectId? = null,
        var tone: String = "",
        var capo: Int = 0,
        var thumb: String? = null,
        var chord: String = ""
)