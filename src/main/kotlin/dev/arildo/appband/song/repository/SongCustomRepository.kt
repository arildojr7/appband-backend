package dev.arildo.appband.song.repository

import dev.arildo.appband.song.service.dto.SongResponseDTO

interface SongCustomRepository {

    fun getSongs(): List<SongResponseDTO>
}