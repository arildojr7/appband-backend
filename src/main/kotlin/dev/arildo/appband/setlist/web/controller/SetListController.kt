package dev.arildo.appband.setlist.web.controller

import dev.arildo.appband.setlist.service.SetListService
import dev.arildo.appband.setlist.service.dto.AddSetListRequestDTO
import dev.arildo.appband.setlist.service.dto.SetListDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SetListController(private val setListService: SetListService) {

    @GetMapping("setlist")
    fun getSetLists() : ResponseEntity<List<SetListDTO>> {
        return ResponseEntity.ok(setListService.getSetLists())
    }

    @PostMapping("setlist")
    fun addSetList(@RequestBody request: AddSetListRequestDTO) : ResponseEntity<SetListDTO> {
        return ResponseEntity.ok(setListService.addSetList(request))
    }
}