package com.capacitorjs.community.plugins.bluetoothle

import java.util.concurrent.atomic.AtomicBoolean

/**
 * Thread-safe executor that ensures a callback is invoked exactly once.
 *
 * Prevents race conditions where both success and error callbacks might fire,
 * or where a timeout and normal completion race. Uses AtomicBoolean with
 * compare-and-set for thread-safe single execution.
 *
 * Usage:
 *   val executor = SafeExecutor(onSuccess, onError)
 *   executor.success(result)  // Only first call executes
 *   executor.error(error)     // Ignored if success was called first
 */
class SafeExecutor<T>(
    private val onSuccess: ((T) -> Unit)?,
    private val onError: ((String) -> Unit)?
) {
    private val wasExecuted = AtomicBoolean(false)

    /**
     * Execute the success callback with the given result.
     * Does nothing if the executor has already been invoked.
     *
     * @param result The success result to pass to the callback
     * @return true if the callback was executed, false if already executed
     */
    fun success(result: T): Boolean {
        if (wasExecuted.compareAndSet(false, true)) {
            onSuccess?.invoke(result)
            return true
        }
        return false
    }

    /**
     * Execute the error callback with the given message.
     * Does nothing if the executor has already been invoked.
     *
     * @param message The error message to pass to the callback
     * @return true if the callback was executed, false if already executed
     */
    fun error(message: String): Boolean {
        if (wasExecuted.compareAndSet(false, true)) {
            onError?.invoke(message)
            return true
        }
        return false
    }

    /**
     * Check if the executor has been invoked.
     */
    fun wasInvoked(): Boolean {
        return wasExecuted.get()
    }
}
