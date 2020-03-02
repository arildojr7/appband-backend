package dev.arildo.appband.song.service.dto

data class SongResponseDTO(
        var id: String? = null,
        var title: String? = null,
        var singer: String? = null,
        var tone: String? = null,
        var capo: Int = 0,
        var thumb: String? = null,
        var chord: String? = null
)