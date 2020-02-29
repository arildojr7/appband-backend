package dev.arildo.appband.user.service.exception

import dev.arildo.appband.shared.service.exception.ApplicationRuntimeException

class ApplicationUserException (msg: String) : ApplicationRuntimeException(msg = msg)