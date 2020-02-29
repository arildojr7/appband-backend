package dev.arildo.appband.config.global

import org.modelmapper.ModelMapper
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ModelMapperConfiguration {

    @Bean
    fun modelMapper(): ModelMapper {
        return ModelMapper().apply {
            configuration.matchingStrategy = MatchingStrategies.STRICT
        }

    }
}