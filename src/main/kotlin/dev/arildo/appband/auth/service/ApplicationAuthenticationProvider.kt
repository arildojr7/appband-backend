package dev.arildo.appband.auth.service

import dev.arildo.appband.auth.authentication.ApplicationUserAuthenticationToken
import dev.arildo.appband.shared.util.MessageUtils
import dev.arildo.appband.user.model.ApplicationUser
import dev.arildo.appband.user.repository.ApplicationUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class ApplicationAuthenticationProvider @Autowired constructor(
        private val passwordEncoder: PasswordEncoder,
        private val applicationUserRepository: ApplicationUserRepository,
        private val messageUtils: MessageUtils) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication?): Authentication {
        return authenticateApplicationUser(authentication)
    }

    override fun supports(tokenClass: Class<*>?): Boolean {
        return ApplicationUserAuthenticationToken::class.java.isAssignableFrom(tokenClass)
    }

    private fun authenticateApplicationUser(authentication: Authentication?): Authentication {
        val jwtAuth = authentication as ApplicationUserAuthenticationToken
        val email = jwtAuth.principal.toString()

        // TODO add base64 encode
        val password = jwtAuth.credentials.toString()
        //val password = String(Base64.getDecoder().decode(encodedPassword))

        // Get user from database and throw exception if not found
        val user = getUser(email)
                ?: throw UsernameNotFoundException(messageUtils.getMessage("auth.service.user.not.found", email))

        // Performs login
        if (!passwordEncoder.matches(password, user.password)) {
            throw  BadCredentialsException(messageUtils.getMessage("auth.service.incorrect.password"))
        }
        return ApplicationUserAuthenticationToken(user)
    }

    private fun getUser(email: String): ApplicationUser? {
        return applicationUserRepository.findByEmail(email)
    }

}