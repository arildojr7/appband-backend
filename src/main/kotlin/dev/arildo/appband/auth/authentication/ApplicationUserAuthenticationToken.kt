package dev.arildo.appband.auth.authentication

import dev.arildo.appband.user.model.ApplicationUser
import dev.arildo.appband.auth.dto.ApplicationUserCredentials
import dev.arildo.appband.auth.service.JwtService
import dev.arildo.appband.user.model.Authority
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class ApplicationUserAuthenticationToken : UsernamePasswordAuthenticationToken {
    var userId: String? = ""
    var authorities: Set<Authority> = setOf()

    /**
     * Constructor for before authentication.
     *
     * @param credentials [ApplicationUserCredentials] object.
     */
    constructor(credentials: ApplicationUserCredentials) : super(credentials.email, credentials.password) {
    }

    /**
     * Constructor for after authentication.
     *
     * @param user [ApplicationUser] object for authenticated user.
     */
    constructor(user: ApplicationUser?) : super(user?.id, null, user?.authorities) {
        if (user != null) {
            this.userId = user.id
            this.authorities = user.authorities
        }
    }

    /**
     * Constructor for authorization.
     *
     * @param decodedJwt [ApplicationUserDecodedJwt] object containing info from JWT on request.
     */
    constructor(decodedJwt: ApplicationUserDecodedJwt) : this(decodedJwt.user)
}