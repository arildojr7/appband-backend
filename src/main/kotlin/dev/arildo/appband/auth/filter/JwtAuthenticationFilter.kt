package dev.arildo.appband.auth.filter

import dev.arildo.appband.auth.authentication.ApplicationUserAuthenticationToken
import dev.arildo.appband.auth.config.WebSecurityConfig.BasicWebSecurityConfig
import dev.arildo.appband.auth.dto.ApplicationUserCredentials
import dev.arildo.appband.auth.dto.AuthenticationResponse
import dev.arildo.appband.shared.dto.GenericResponse
import dev.arildo.appband.auth.service.ApplicationAuthenticationProvider
import dev.arildo.appband.auth.service.JwtService
import dev.arildo.appband.shared.util.MessageUtils
import dev.arildo.appband.shared.util.log
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(authenticationManager: AuthenticationManager,
                              private val objectMapper: ObjectMapper,
                              private val jwtService: JwtService,
                              private val messageUtils: MessageUtils) : UsernamePasswordAuthenticationFilter() {
    init {
        setAuthenticationManager(authenticationManager)
    }

    companion object {
        val log = log()
    }

    /**
     * This method immediately invoked when user sends POST request to /login or /admin/login.
     */
    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication {
        try {
            // Check if request body has content
            val requestBody = request.inputStream
            if (requestBody == null || requestBody.isFinished) {
                throw AuthenticationCredentialsNotFoundException(messageUtils.getMessage("auth.service.missing.credentials"))
            }

            val credentials = objectMapper.readValue(requestBody, ApplicationUserCredentials::class.java)
            val auth = ApplicationUserAuthenticationToken(credentials)

            log.debug("Authentication attempt - credentials = {}", credentials)

            /**
             * Delegates the authentication to [ApplicationAuthenticationProvider] that was setup on
             * [BasicWebSecurityConfig.configure]
             */
            return authenticationManager.authenticate(auth)
        } catch (e: IOException) {
            throw AuthenticationServiceException(messageUtils.getMessage("auth.service.error.reading.user.credentials"))
        }

    }

    /**
     * This method is invoked if [@link [AuthenticationManager.authenticate] is successful.
     */
    @Throws(IOException::class)
    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain,
                                          authResult: Authentication) {
        // Build response body
        val responseBody: AuthenticationResponse
        val authToken = authResult as ApplicationUserAuthenticationToken
        log.debug("Generating JWT token for user {}", authToken.name)
        val jwtToken = jwtService.generateApplicationUserJwt(authToken.userId, authToken.authorities)
        // Add JWT token and user data to response body
        responseBody = AuthenticationResponse(jwtToken)

        response.status = HttpStatus.OK.value()
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        objectMapper.writeValue(response.writer, responseBody)
    }

    @Throws(IOException::class)
    override fun unsuccessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, e: AuthenticationException) {
        log.debug("Authentication failure - {}: {} ", e.javaClass.simpleName, e.message)
        val status = if (e is AuthenticationServiceException)
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        else
            HttpStatus.UNAUTHORIZED.value()

        val responseBody: String = objectMapper.writeValueAsString(GenericResponse(e.message))

        response.status = status
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        response.writer.write(responseBody)
    }

}