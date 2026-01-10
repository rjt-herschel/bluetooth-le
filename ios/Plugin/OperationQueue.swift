import Foundation

/// Thread-safe operation queue for serializing BLE GATT operations.
///
/// iOS's CoreBluetooth can handle concurrent operations better than Android,
/// but serializing operations still improves reliability by preventing
/// race conditions and ensuring deterministic behavior.
///
/// Usage:
///   queue.enqueue { completion in
///       // Perform BLE operation
///       peripheral.readValue(for: characteristic)
///       // Call completion() when done (in delegate callback)
///   }
class OperationQueue {
    typealias OperationBlock = (@escaping () -> Void) -> Void

    private var operations: [OperationBlock] = []
    private var isRunning = false
    private let lock = NSLock()
    private var currentCompletion: (() -> Void)?

    /// Enqueue a BLE operation. The operation will be executed when all
    /// previous operations have completed.
    ///
    /// - Parameter operation: A closure that receives a completion handler.
    ///                        The operation MUST call the completion handler
    ///                        when finished (success or failure), otherwise
    ///                        the queue will stall.
    func enqueue(_ operation: @escaping OperationBlock) {
        lock.lock()
        operations.append(operation)
        lock.unlock()
        executeNext()
    }

    /// Execute the next operation in the queue if no operation is running.
    private func executeNext() {
        lock.lock()
        guard !isRunning, !operations.isEmpty else {
            lock.unlock()
            return
        }
        isRunning = true
        let operation = operations.removeFirst()
        lock.unlock()

        let completion: () -> Void = { [weak self] in
            self?.lock.lock()
            self?.isRunning = false
            self?.currentCompletion = nil
            self?.lock.unlock()
            self?.executeNext()
        }

        lock.lock()
        currentCompletion = completion
        lock.unlock()

        operation(completion)
    }

    /// Complete the current operation and proceed to the next one.
    /// Call this from resolve/reject methods.
    func completeCurrentOperation() {
        lock.lock()
        let completion = currentCompletion
        lock.unlock()
        completion?()
    }

    /// Clear all pending operations. Does not affect the currently running operation.
    func clear() {
        lock.lock()
        operations.removeAll()
        lock.unlock()
    }

    /// Check if the queue has pending operations.
    var hasPending: Bool {
        lock.lock()
        defer { lock.unlock() }
        return !operations.isEmpty || isRunning
    }
}
