package dev.arildo.appband.auth.filter

import dev.arildo.appband.auth.utils.BASIC_PREFIX
import dev.arildo.appband.auth.utils.decodeCredentials
import dev.arildo.appband.shared.dto.GenericResponse
import dev.arildo.appband.shared.util.MessageUtils
import dev.arildo.appband.shared.util.log
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ApplicationBasicAuthenticationFilter(private val objectMapper: ObjectMapper,
                                           private val appName: String?,
                                           private val appPassword: String?,
                                           private val messageUtils: MessageUtils) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val header = request.getHeader(AUTHORIZATION)
        log.debug("Basic Authentication attempt - Header = {}", header)

        try {
            // Authorization header value must start with "Basic "
            if (StringUtils.hasText(header) && header.startsWith(BASIC_PREFIX)) {
                val appCredentials = "$appName:$appPassword"
                val credentials = decodeCredentials(header.removePrefix(BASIC_PREFIX), messageUtils)

                if (appCredentials == credentials) {
                    log.debug("Credentials match")
                    chain.doFilter(request, response)
                } else {
                    log.debug("Expected credentials = {} ", appCredentials)
                    throw BadCredentialsException(messageUtils.getMessage("auth.service.invalid.basic.credentials"))
                }
            } else {
                throw AuthenticationCredentialsNotFoundException(messageUtils.getMessage("auth.service.missing.basic.credentials"))
            }

        } catch (e: AuthenticationException) {
            onUnsuccessfulAuthentication(response, e)
        }

    }

    private fun onUnsuccessfulAuthentication(response: HttpServletResponse, e: AuthenticationException) {
        log.debug("Authentication failure - {} = {} ", e.javaClass.simpleName, e.message)
        val status = if (e is BadCredentialsException) HttpStatus.UNAUTHORIZED else HttpStatus.FORBIDDEN

        response.status = status.value()
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        objectMapper.writeValue(response.writer, GenericResponse(e.message))
    }

    companion object {
        val log = log()
    }
}