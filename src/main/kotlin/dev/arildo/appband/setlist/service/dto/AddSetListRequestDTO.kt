package dev.arildo.appband.setlist.service.dto

class AddSetListRequestDTO(
        var date: Long = 0L,
        var songs: List<String> = emptyList()
)