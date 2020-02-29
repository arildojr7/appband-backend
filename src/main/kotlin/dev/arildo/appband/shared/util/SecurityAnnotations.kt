package dev.arildo.appband.shared.util

import org.springframework.security.access.annotation.Secured

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Secured("ROLE_ADMIN")
annotation class IsAdmin

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Secured("ROLE_USER")
annotation class IsUser

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Secured("ROLE_ADMIN", "ROLE_USER")
annotation class IsAdminOrUser
