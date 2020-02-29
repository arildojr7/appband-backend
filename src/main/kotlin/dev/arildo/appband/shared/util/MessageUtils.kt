package dev.arildo.appband.shared.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.Locale

@Component
class MessageUtils @Autowired constructor(private val messageSource: MessageSource) {

    fun getMessage(key: String, vararg arguments: String): String {
        return messageSource.getMessage(key, arguments, Locale.getDefault())
    }
}