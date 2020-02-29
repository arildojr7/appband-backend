package dev.arildo.appband.user.web.controller

import dev.arildo.appband.user.service.UserService
import dev.arildo.appband.user.service.dto.RegisterRequestDTO
import dev.arildo.appband.user.service.dto.RegisterResponseDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun register(@RequestBody @Valid request: RegisterRequestDTO): ResponseEntity<RegisterResponseDTO> {
        return ResponseEntity.ok(userService.register(request))
    }
}