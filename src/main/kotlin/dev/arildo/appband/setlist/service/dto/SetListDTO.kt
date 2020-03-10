package dev.arildo.appband.setlist.service.dto

import dev.arildo.appband.musician.service.dto.MusicianDTO
import dev.arildo.appband.song.service.dto.SongResponseDTO

class SetListDTO(
        var id: String? = null,
        var number: Int? = null,
        var date: Long? = 0L,
        var musician: List<MusicianDTO> = emptyList(),
        var song: List<SongResponseDTO> = emptyList()
)