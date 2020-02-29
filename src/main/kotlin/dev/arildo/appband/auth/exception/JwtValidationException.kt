package dev.arildo.appband.auth.exception

import org.springframework.security.core.AuthenticationException

class JwtValidationException(msg: String?) : AuthenticationException(msg) {

}
