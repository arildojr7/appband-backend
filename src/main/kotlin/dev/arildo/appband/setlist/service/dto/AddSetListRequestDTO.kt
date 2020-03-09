package dev.arildo.appband.setlist.service.dto

class AddSetListRequestDTO(
        var date: Long = 0L,
        var musicians: List<String> = emptyList(),
        var songs: List<String> = emptyList()
)