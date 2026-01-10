package com.capacitorjs.community.plugins.bluetoothle

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Thread-safe operation queue for serializing BLE GATT operations.
 *
 * Android's BLE stack requires GATT operations to be serialized - attempting
 * concurrent operations can cause failures or undefined behavior. This queue
 * ensures operations execute one at a time.
 *
 * Usage:
 *   queue.enqueue { callback ->
 *       // Perform GATT operation
 *       bluetoothGatt.readCharacteristic(characteristic)
 *       // Call callback.complete() when done (in GATT callback)
 *   }
 */
class OperationQueue {

    private data class Operation(
        val execute: (OperationCallback) -> Unit
    )

    interface OperationCallback {
        fun complete()
    }

    private val queue = ConcurrentLinkedQueue<Operation>()
    private val isRunning = AtomicBoolean(false)

    /**
     * Enqueue a GATT operation. The operation will be executed when all
     * previous operations have completed.
     *
     * @param operation A function that receives a callback. The operation MUST
     *                  call callback.complete() when the operation finishes
     *                  (success or failure), otherwise the queue will stall.
     */
    fun enqueue(operation: (OperationCallback) -> Unit) {
        queue.add(Operation(operation))
        executeNext()
    }

    /**
     * Execute the next operation in the queue if no operation is running.
     */
    private fun executeNext() {
        if (!isRunning.compareAndSet(false, true)) {
            // Another operation is already running
            return
        }

        val operation = queue.poll()
        if (operation == null) {
            isRunning.set(false)
            return
        }

        val callback = object : OperationCallback {
            override fun complete() {
                isRunning.set(false)
                executeNext()
            }
        }

        try {
            operation.execute(callback)
        } catch (e: Exception) {
            // If operation throws, ensure we don't stall the queue
            callback.complete()
            throw e
        }
    }

    /**
     * Clear all pending operations. Does not affect the currently running operation.
     */
    fun clear() {
        queue.clear()
    }

    /**
     * Check if the queue has pending operations.
     */
    fun hasPending(): Boolean {
        return queue.isNotEmpty() || isRunning.get()
    }
}
