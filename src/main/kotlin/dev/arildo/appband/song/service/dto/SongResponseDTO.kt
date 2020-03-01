package dev.arildo.appband.song.service.dto

class SongResponseDTO(
        var id: String? = null,
        var title: String = "",
        var singerName: String = "",
        var tone: String = "",
        var capo: Int = 0,
        var thumb: String? = null,
        var chord: String = ""
)