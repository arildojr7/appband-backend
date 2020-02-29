package dev.arildo.appband.config.global

import dev.arildo.appband.shared.util.log
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.lang.reflect.Method
import java.util.concurrent.Executor

@Configuration
@EnableAsync
class AsyncConfiguration : AsyncConfigurer {

    companion object {
        private val log = log()
    }

    override fun getAsyncExecutor(): Executor? {
        val executor = ThreadPoolTaskExecutor()
        executor.setThreadNamePrefix("async-")
        executor.corePoolSize = 10
        executor.maxPoolSize = 20
        executor.setQueueCapacity(20)
        executor.keepAliveSeconds = 120

        // Wait at maximum 5 minutes to start shutting down container if there are tasks on the queue
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.setAwaitTerminationSeconds(300)
        executor.initialize()

        return executor
    }

    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler? {
        return CustomAsyncUncaughtExceptionHandler()
    }

    private class CustomAsyncUncaughtExceptionHandler : AsyncUncaughtExceptionHandler {

        override fun handleUncaughtException(throwable: Throwable, method: Method, vararg objects: Any) {
            val methodName = method.declaringClass.simpleName + "#" + method.name
            val args = listOf(objects)
            log.error("An error occurred while executing the async method {} (args = {})", methodName, args, throwable)
        }
    }
}