package dev.arildo.appband.auth.filter

import dev.arildo.appband.auth.authentication.ApplicationUserAuthenticationToken
import dev.arildo.appband.auth.authentication.ApplicationUserDecodedJwt
import dev.arildo.appband.auth.exception.JwtNotFoundException
import dev.arildo.appband.auth.exception.JwtValidationException
import dev.arildo.appband.auth.service.JwtService
import dev.arildo.appband.auth.utils.BEARER_PREFIX
import dev.arildo.appband.shared.dto.GenericResponse
import dev.arildo.appband.shared.util.MessageUtils
import dev.arildo.appband.shared.util.log
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(private val objectMapper: ObjectMapper,
                             private val jwtService: JwtService,
                             private val messageUtils: MessageUtils) : OncePerRequestFilter() {

    companion object {
        val log = log()
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        request.getHeader(AUTHORIZATION)?.let { header ->
            log.debug("JWT Authorization - Header = {}", header)

            // Authorization header value must start with "Bearer "
            if (header.startsWith(BEARER_PREFIX)) {
                try {
                    val auth = authorizeApplicationUser(header)

                    (auth as UsernamePasswordAuthenticationToken).details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = auth

                    // Request is forward only if authorization is successful
                    chain.doFilter(request, response)

                } catch (e: JWTVerificationException) {
                    val msg: String? = when (e) {
                        is TokenExpiredException -> messageUtils.getMessage("auth.service.expired.jwt")
                        is JWTDecodeException -> messageUtils.getMessage("auth.service.invalid.jwt")
                        else -> e.message
                    }
                    onUnsuccessfulAuthorization(response, JwtValidationException(msg))
                }

            } else {
                onUnsuccessfulAuthorization(response, JwtNotFoundException(messageUtils.getMessage("auth.service.missing.credentials")))
            }
        } ?: run {
            onUnsuccessfulAuthorization(response, JwtNotFoundException(messageUtils.getMessage("auth.service.missing.credentials")))
        }
    }

    private fun authorizeApplicationUser(authorizationHeader: String): Authentication {
        val decodedJwt = ApplicationUserDecodedJwt(jwtService.verifyJwt(authorizationHeader))
        return ApplicationUserAuthenticationToken(decodedJwt)
    }

    private fun onUnsuccessfulAuthorization(response: HttpServletResponse, e: AuthenticationException) {
        log.debug("Authorization failure - {}: {} ", e.javaClass.simpleName, e.message)
        val status = if (e is JwtNotFoundException) HttpStatus.FORBIDDEN.value() else HttpStatus.UNAUTHORIZED.value()

        response.status = status
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        objectMapper.writeValue(response.writer, GenericResponse(e.message))
    }
}