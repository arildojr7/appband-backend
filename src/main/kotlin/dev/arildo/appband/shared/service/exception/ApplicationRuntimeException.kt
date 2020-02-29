package dev.arildo.appband.shared.service.exception

open class ApplicationRuntimeException(var description: String = "", var code: String = "", msg: String?) : RuntimeException(msg)