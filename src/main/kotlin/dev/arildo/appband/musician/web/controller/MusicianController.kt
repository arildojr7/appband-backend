package dev.arildo.appband.musician.web.controller

import dev.arildo.appband.musician.service.MusicianService
import dev.arildo.appband.musician.service.dto.AddMusicianRequestDTO
import dev.arildo.appband.musician.service.dto.MusicianDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MusicianController(private val musicianService: MusicianService) {

    @GetMapping("musician")
    fun getMusicians(): ResponseEntity<List<MusicianDTO>> {
        return ResponseEntity.ok(musicianService.getMusicians())
    }

    @PostMapping("musician")
    fun addMusician(@RequestBody request: AddMusicianRequestDTO): ResponseEntity<MusicianDTO> {
        return ResponseEntity.ok(musicianService.addMusician(request))
    }
}