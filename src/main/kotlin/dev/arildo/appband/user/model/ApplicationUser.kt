package dev.arildo.appband.user.model

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("applicationUser")
@JsonInclude(JsonInclude.Include.NON_NULL)
class ApplicationUser(
        @Id
        var id: String? = null,
        var email: String = "",
        var password: String = "",
        var name: String? = null,
        var authorities: Set<Authority> = setOf()
)