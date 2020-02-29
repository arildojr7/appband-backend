package dev.arildo.appband.shared.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class GenericResponse(
        var code: String? = null,
        var message: String? = "",
        var details: List<String>? = null,
        var status: String? = null,
        var description: String? = null
)
