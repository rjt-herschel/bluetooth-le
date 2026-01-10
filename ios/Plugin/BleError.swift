import Foundation

/// Structured error codes for BLE operations.
/// These codes allow applications to handle specific failure modes programmatically.
enum BleErrorCode: Int {
    // General (0-99)
    case unknownError = 0
    case bluetoothUnavailable = 1
    case bluetoothDisabled = 2
    case permissionDenied = 3

    // Scanning (100-199)
    case scanAlreadyActive = 100
    case scanFailed = 101

    // Connection (200-299)
    case deviceNotFound = 200
    case connectionFailed = 201
    case connectionTimeout = 202
    case deviceDisconnected = 203
    case unexpectedDisconnect = 204

    // Service Discovery (300-399)
    case serviceNotFound = 300
    case serviceDiscoveryFailed = 301
    case serviceDiscoveryTimeout = 302

    // Characteristics (400-499)
    case characteristicNotFound = 400
    case characteristicReadFailed = 401
    case characteristicWriteFailed = 402
    case characteristicNotifyFailed = 403
    case operationTimeout = 404

    // Descriptors (500-599)
    case descriptorNotFound = 500
    case descriptorReadFailed = 501
    case descriptorWriteFailed = 502

    var defaultMessage: String {
        switch self {
        case .unknownError: return "Unknown error"
        case .bluetoothUnavailable: return "Bluetooth unavailable"
        case .bluetoothDisabled: return "Bluetooth disabled"
        case .permissionDenied: return "Bluetooth permission denied"
        case .scanAlreadyActive: return "Scan already active"
        case .scanFailed: return "Scan failed"
        case .deviceNotFound: return "Device not found"
        case .connectionFailed: return "Connection failed"
        case .connectionTimeout: return "Connection timeout"
        case .deviceDisconnected: return "Device disconnected"
        case .unexpectedDisconnect: return "Unexpected disconnect"
        case .serviceNotFound: return "Service not found"
        case .serviceDiscoveryFailed: return "Service discovery failed"
        case .serviceDiscoveryTimeout: return "Service discovery timeout"
        case .characteristicNotFound: return "Characteristic not found"
        case .characteristicReadFailed: return "Characteristic read failed"
        case .characteristicWriteFailed: return "Characteristic write failed"
        case .characteristicNotifyFailed: return "Characteristic notify failed"
        case .operationTimeout: return "Operation timeout"
        case .descriptorNotFound: return "Descriptor not found"
        case .descriptorReadFailed: return "Descriptor read failed"
        case .descriptorWriteFailed: return "Descriptor write failed"
        }
    }
}

/// A structured BLE error with code and message.
struct BleError {
    let code: BleErrorCode
    let message: String

    init(_ code: BleErrorCode, _ message: String? = nil) {
        self.code = code
        self.message = message ?? code.defaultMessage
    }

    func toDict() -> [String: Any] {
        return [
            "code": code.rawValue,
            "message": message
        ]
    }
}
