package dev.arildo.appband.musician.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("musician")
class Musician(
        @Id
        var id: String? = null,
        var name: String = ""
)