package dev.arildo.appband.auth.service

import dev.arildo.appband.auth.authentication.ApplicationUserDecodedJwt
import dev.arildo.appband.auth.utils.AUTHORITIES
import dev.arildo.appband.auth.utils.BEARER_PREFIX
import dev.arildo.appband.auth.utils.SECRET
import dev.arildo.appband.user.model.Authority
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.impl.PublicClaims.AUDIENCE
import com.auth0.jwt.impl.PublicClaims.ISSUER
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService(@param:Value("\${app.env.is-homolog}") private val isHomolog: Boolean) {

    fun generateApplicationUserJwt(userId: String?, userAuthorities: Set<Authority>?): String {
        val now = System.currentTimeMillis()
        // Convert user.authorities from Set<Role> to String[]
        val authorities = getAuthoritiesAsArray(userAuthorities)

        val jwtBuilder = JWT.create()
                // Registered claims
                .withIssuer(ISSUER)
                .withSubject(userId)
                .withAudience(AUDIENCE)
                .withIssuedAt(Date(now))
                // Private claims
                .withArrayClaim(AUTHORITIES, authorities)

        val token = jwtBuilder.sign(Algorithm.HMAC512(SECRET))
        return BEARER_PREFIX + token
    }

    fun verifyJwt(authorization: String): DecodedJWT {
        return JWT.require(Algorithm.HMAC512(SECRET))
                .withIssuer(ISSUER)
                .withAudience(AUDIENCE)
                .build()
                .verify(authorization.replace(BEARER_PREFIX, ""))
    }

    fun decodeApplicationUserJwt(authorization: String): ApplicationUserDecodedJwt {
        return ApplicationUserDecodedJwt(JWT.decode(authorization.replace(BEARER_PREFIX, "")))
    }

    private fun getAuthoritiesAsArray(authorities: Set<Authority>?): Array<String?> {
        return authorities?.map { it.authority }?.toTypedArray<String?>() ?: arrayOfNulls<String>(0)
    }
}