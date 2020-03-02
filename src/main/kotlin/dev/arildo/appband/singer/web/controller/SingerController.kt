package dev.arildo.appband.singer.web.controller

import dev.arildo.appband.singer.service.SingerService
import dev.arildo.appband.singer.service.dto.AddSingerRequestDTO
import dev.arildo.appband.singer.service.dto.SingerDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SingerController(private val singerService: SingerService) {

    @GetMapping("singer")
    fun getSingers(): ResponseEntity<List<SingerDTO>> {
        return ResponseEntity.ok(singerService.getSingers())
    }

    @PostMapping("singer")
    fun addSinger(@RequestBody request: AddSingerRequestDTO): ResponseEntity<SingerDTO> {
        return ResponseEntity.ok(singerService.addSinger(request))
    }
}