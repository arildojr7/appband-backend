package dev.arildo.appband.setlist.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.util.*

class SetList(
        @Id
        var id: String? = null,
        var date: Date? = null,
        var songIds: List<ObjectId> = emptyList()
)