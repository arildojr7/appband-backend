package dev.arildo.appband.auth.authentication

import dev.arildo.appband.auth.utils.AUTHORITIES
import dev.arildo.appband.user.model.ApplicationUser
import dev.arildo.appband.user.model.Authority
import com.auth0.jwt.interfaces.DecodedJWT

class ApplicationUserDecodedJwt(jwt: DecodedJWT) {
    private val authorities = Authority.fromArray(jwt.getClaim(AUTHORITIES).asArray(String::class.java))
    var user = ApplicationUser(jwt.subject, authorities = authorities)
}