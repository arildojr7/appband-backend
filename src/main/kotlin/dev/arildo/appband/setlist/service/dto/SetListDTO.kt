package dev.arildo.appband.setlist.service.dto

import dev.arildo.appband.song.service.dto.SongResponseDTO

class SetListDTO(
        var id: String? = null,
        var number: Int? = null,
        var date: Long? = 0L,
        var song: List<SongResponseDTO> = emptyList()
)