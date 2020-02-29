package dev.arildo.appband.auth.config

import dev.arildo.appband.auth.filter.ApplicationBasicAuthenticationFilter
import dev.arildo.appband.auth.filter.GuestAuthorizationFilter
import dev.arildo.appband.auth.filter.JwtAuthenticationFilter
import dev.arildo.appband.auth.filter.JwtAuthorizationFilter
import dev.arildo.appband.auth.service.ApplicationAuthenticationProvider
import dev.arildo.appband.auth.service.JwtService
import dev.arildo.appband.shared.util.MessageUtils
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true
)
class WebSecurityConfig {

    // region BASIC FILTER
    @Configuration
    @Order(1)
    class BasicWebSecurityConfig @Autowired
    constructor(private val applicationAuthenticationProvider: ApplicationAuthenticationProvider,
                private val objectMapper: ObjectMapper,
                private val jwtService: JwtService,
                private val messageUtils: MessageUtils) : WebSecurityConfigurerAdapter() {

        @Value("\${app.security.user.client-id}")
        private val appName: String? = null

        @Value("\${app.security.user.client-secret}")
        private val appPassword: String? = null

        override fun configure(http: HttpSecurity) {
            http
                    .cors()
                    .and()
                    .requestMatchers()
                    // Routes that require application basic authentication
                    .antMatchers("/register/**", "/login/**") // TODO review this access rules
                    .and()
                    .authorizeRequests()
                    .anyRequest().permitAll()
                    .and()
                    // Filter for Basic authentication
                    .addFilterBefore(basicAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
                    // Filter for /login request
                    .addFilter(jwtAuthenticationFilter())
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        /**
         * Configure [AuthenticationManagerBuilder] to use the [ApplicationAuthenticationProvider] to authenticate the users.
         */
        override fun configure(auth: AuthenticationManagerBuilder) {
            auth.authenticationProvider(applicationAuthenticationProvider)
        }

        private fun basicAuthenticationFilter(): ApplicationBasicAuthenticationFilter {
            return ApplicationBasicAuthenticationFilter(objectMapper, appName, appPassword, messageUtils)
        }

        @Throws(Exception::class)
        private fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
            return JwtAuthenticationFilter(authenticationManager(), objectMapper, jwtService, messageUtils)
        }

    }

    // endregion



    // region JWT FILTER
    /**
     * Configuration for JWT validation.
     */
    @Configuration
    @Order(2)
    class JwtWebSecurityConfig @Autowired
    constructor(private val objectMapper: ObjectMapper,
                private val jwtService: JwtService,
                private val messageUtils: MessageUtils) : WebSecurityConfigurerAdapter() {

        override fun configure(http: HttpSecurity) {
            http
                    .cors()
                    .and()
                    // Captures any request that was not captured on config 1
                    .antMatcher("/**")
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    // Filter all request and validate JWT token
                    .addFilterAt(jwtAuthorizationFilter(), BasicAuthenticationFilter::class.java)
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        private fun jwtAuthorizationFilter(): JwtAuthorizationFilter {
            return JwtAuthorizationFilter(objectMapper, jwtService, messageUtils)
        }
    }

    // endregion
}