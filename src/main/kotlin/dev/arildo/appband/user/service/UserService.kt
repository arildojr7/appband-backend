package dev.arildo.appband.user.service

import dev.arildo.appband.auth.service.JwtService
import dev.arildo.appband.shared.util.MessageUtils
import dev.arildo.appband.shared.util.log
import dev.arildo.appband.user.model.ApplicationUser
import dev.arildo.appband.user.model.Authority
import dev.arildo.appband.user.repository.ApplicationUserRepository
import dev.arildo.appband.user.service.dto.RegisterRequestDTO
import dev.arildo.appband.user.service.dto.RegisterResponseDTO
import dev.arildo.appband.user.service.exception.ApplicationUserException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val applicationUserRepository: ApplicationUserRepository,
                  private val jwtService: JwtService,
                  private val passwordEncoder: PasswordEncoder,
                  private val messageUtils: MessageUtils
) {

    companion object {
        val log = log()
    }

    // region REGISTER
    fun register(request: RegisterRequestDTO): RegisterResponseDTO {

        // verify if email already in use
        if (applicationUserRepository.findByEmail(request.email) != null) {
            throw ApplicationUserException(messageUtils.getMessage("auth.service.user.email.already.registered"))
        }

        applicationUserRepository.save(
                ApplicationUser(
                        email = request.email,
                        name = request.name,
                        password = passwordEncoder.encode(request.password),
                        authorities = setOf(Authority.ROLE_USER)
                )
        )
        // TODO add token response
        return RegisterResponseDTO()
    }
    // endregion

}