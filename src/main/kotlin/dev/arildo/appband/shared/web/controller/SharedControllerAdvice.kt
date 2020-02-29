package dev.arildo.appband.shared.web.controller

import dev.arildo.appband.shared.dto.GenericResponse
import dev.arildo.appband.shared.service.exception.ApplicationRuntimeException
import dev.arildo.appband.shared.service.exception.MultipartFileException
import dev.arildo.appband.shared.util.log
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class SharedControllerAdvice {
    private val log = log()

    @ExceptionHandler(value = [ApplicationRuntimeException::class])
    fun handleException(e: ApplicationRuntimeException): ResponseEntity<*> {
        log.error(e.message)
        log.error(e.description, e.code)
        return ResponseEntity.badRequest().body(GenericResponse(
                message = if (e.code.isNotBlank()) "${e.message} [${e.code}]" else e.message,
                description = if (e.description.isNotBlank()) e.description else null)
        )
    }

    @ExceptionHandler(value = [MultipartFileException::class])
    fun handleException(e: MultipartFileException){
        log.error(e.message, e)
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun handleException(e: IllegalArgumentException) : ResponseEntity<*>  {
        log.error(e.message, e)
        return ResponseEntity.badRequest().body(GenericResponse(message =  e.message))
    }
}