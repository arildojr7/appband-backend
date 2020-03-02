package dev.arildo.appband.singer.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("singer")
class Singer(
        @Id
        var id: String? = null,
        var name: String = ""
)