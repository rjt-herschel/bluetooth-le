package com.capacitorjs.community.plugins.bluetoothle

import com.getcapacitor.JSObject

/**
 * Structured error codes for BLE operations.
 * These codes allow applications to handle specific failure modes programmatically.
 */
enum class BleErrorCode(val code: Int) {
    // General (0-99)
    UnknownError(0),
    BluetoothUnavailable(1),
    BluetoothDisabled(2),
    PermissionDenied(3),

    // Scanning (100-199)
    ScanAlreadyActive(100),
    ScanFailed(101),

    // Connection (200-299)
    DeviceNotFound(200),
    ConnectionFailed(201),
    ConnectionTimeout(202),
    DeviceDisconnected(203),
    UnexpectedDisconnect(204),

    // Service Discovery (300-399)
    ServiceNotFound(300),
    ServiceDiscoveryFailed(301),
    ServiceDiscoveryTimeout(302),

    // Characteristics (400-499)
    CharacteristicNotFound(400),
    CharacteristicReadFailed(401),
    CharacteristicWriteFailed(402),
    CharacteristicNotifyFailed(403),
    OperationTimeout(404),

    // Descriptors (500-599)
    DescriptorNotFound(500),
    DescriptorReadFailed(501),
    DescriptorWriteFailed(502);

    val defaultMessage: String
        get() = when (this) {
            UnknownError -> "Unknown error"
            BluetoothUnavailable -> "Bluetooth unavailable"
            BluetoothDisabled -> "Bluetooth disabled"
            PermissionDenied -> "Bluetooth permission denied"
            ScanAlreadyActive -> "Scan already active"
            ScanFailed -> "Scan failed"
            DeviceNotFound -> "Device not found"
            ConnectionFailed -> "Connection failed"
            ConnectionTimeout -> "Connection timeout"
            DeviceDisconnected -> "Device disconnected"
            UnexpectedDisconnect -> "Unexpected disconnect"
            ServiceNotFound -> "Service not found"
            ServiceDiscoveryFailed -> "Service discovery failed"
            ServiceDiscoveryTimeout -> "Service discovery timeout"
            CharacteristicNotFound -> "Characteristic not found"
            CharacteristicReadFailed -> "Characteristic read failed"
            CharacteristicWriteFailed -> "Characteristic write failed"
            CharacteristicNotifyFailed -> "Characteristic notify failed"
            OperationTimeout -> "Operation timeout"
            DescriptorNotFound -> "Descriptor not found"
            DescriptorReadFailed -> "Descriptor read failed"
            DescriptorWriteFailed -> "Descriptor write failed"
        }
}

/**
 * A structured BLE error with code and message.
 */
data class BleError(
    val code: BleErrorCode,
    val message: String = code.defaultMessage
) {
    fun toJSObject(): JSObject {
        return JSObject().apply {
            put("code", code.code)
            put("message", message)
        }
    }

    override fun toString(): String = message
}
