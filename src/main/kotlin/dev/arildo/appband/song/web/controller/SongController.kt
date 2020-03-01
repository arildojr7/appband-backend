package dev.arildo.appband.song.web.controller

import dev.arildo.appband.song.service.SongService
import dev.arildo.appband.song.service.dto.AddSongRequestDTO
import dev.arildo.appband.song.service.dto.SongResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("song")
class SongController(private val songService: SongService) {

    @GetMapping
    fun getSongs() : ResponseEntity<List<SongResponseDTO>> {
        return ResponseEntity.ok(songService.getSongs())
    }

    @PostMapping
    fun addSong(@RequestBody request: AddSongRequestDTO) : ResponseEntity<SongResponseDTO> {
        return ResponseEntity.ok(songService.addSong(request))
    }
}