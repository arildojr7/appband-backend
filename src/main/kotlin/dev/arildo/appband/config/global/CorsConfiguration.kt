package dev.arildo.appband.config.global

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfiguration : WebMvcConfigurer {

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                        .allowedOrigins( "http://127.0.0.1:3000")
                        .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS")
                        .allowedHeaders("Authorization", "Content-Type")
            }
        }
    }
}