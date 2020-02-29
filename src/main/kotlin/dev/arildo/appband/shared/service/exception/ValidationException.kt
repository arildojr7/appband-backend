package dev.arildo.appband.shared.service.exception

class ValidationException (msg: String?) : ApplicationRuntimeException("", "", msg) {}