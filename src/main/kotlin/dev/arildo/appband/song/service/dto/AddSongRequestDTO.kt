package dev.arildo.appband.song.service.dto

class AddSongRequestDTO (
        val title: String = "",
        val singerId: String = "",
        val tone: String = "",
        val capo: Int = 0,
        val thumb: String? = null,
        val chord: String = ""
)