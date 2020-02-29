package dev.arildo.appband.auth.utils

import dev.arildo.appband.auth.filter.ApplicationBasicAuthenticationFilter
import dev.arildo.appband.shared.util.MessageUtils
import org.springframework.security.authentication.BadCredentialsException
import java.util.Base64

fun decodeCredentials(encodedCredentials: String, messageUtils: MessageUtils): String {
    val base64Token = encodedCredentials.toByteArray(Charsets.UTF_8)

    val decoded: ByteArray
    try {
        decoded = Base64.getDecoder().decode(base64Token)
    } catch (e: IllegalArgumentException) {
        throw BadCredentialsException(messageUtils.getMessage("auth.service.failed.decode.basic.credentials"))
    }

    val credentials = String(decoded, Charsets.UTF_8)
    ApplicationBasicAuthenticationFilter.log.debug("Decoded credentials = {}", credentials)
    return if (!credentials.contains(":")) {
        throw BadCredentialsException(messageUtils.getMessage("auth.service.invalid.basic.credentials"))
    } else {
        credentials
    }
}