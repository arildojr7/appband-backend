package dev.arildo.appband.auth.exception

import org.springframework.security.core.AuthenticationException

class JwtNotFoundException(msg: String?) : AuthenticationException(msg) {

}
