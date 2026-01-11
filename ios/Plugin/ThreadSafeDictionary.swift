class ThreadSafeDictionary<K: Hashable, T> {
    private var dictionary: [K: T] = [:]
    private let queue = DispatchQueue(label: "threadSafeDictionaryQueue", attributes: .concurrent)

    subscript(key: K) -> T? {
        get {
            return queue.sync { dictionary[key] }
        }
        set {
            queue.async(flags: .barrier) { self.dictionary[key] = newValue }
        }
    }

    /// Get all keys in the dictionary.
    var allKeys: [K] {
        return queue.sync { Array(dictionary.keys) }
    }

    /// Get all values in the dictionary.
    var allValues: [T] {
        return queue.sync { Array(dictionary.values) }
    }
}
