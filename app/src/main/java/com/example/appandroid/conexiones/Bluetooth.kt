package com.example.appandroid.conexiones

import android.bluetooth.*

class Bluetooth {

    private val BLUETOOTH_ADAPTER = BluetoothAdapter.getDefaultAdapter()

    private fun check_bluetooth(bluetoothDevice: BluetoothDevice) {
        if (!BLUETOOTH_ADAPTER.isEnabled) {
            throw Exception("Bluetooth apagado")
        }
        if (bluetoothDevice == null) {
            throw Exception("Dispositivo no encontrado")
        }
    }

    fun obtener_dispositivos_vinculados(): Set<BluetoothDevice> {
        return BLUETOOTH_ADAPTER.bondedDevices
    }

    fun obtener_dispositivo_por_nombre(nombre: String): BluetoothDevice? {
        for (device in obtener_dispositivos_vinculados()) {
            if (device.name == nombre) {
                return device
            }
        }
        return null
    }

    fun enviar_char(device: BluetoothDevice, char: Char) {
        check_bluetooth(device)
        val socket = device.createRfcommSocketToServiceRecord(device.uuids[0].uuid)
        socket.connect()
        val outputStream = socket.outputStream
        outputStream.write(char.code)
        outputStream.close()
        socket.close()
    }

    fun solicitar_datos_humedad(device: BluetoothDevice): String {
        check_bluetooth(device)
        val socket = device.createRfcommSocketToServiceRecord(device.uuids[0].uuid)
        socket.connect()
        val outputStream = socket.outputStream
        outputStream.write('H'.code)
        outputStream.close()
        val inputStream = socket.inputStream
        val data = inputStream.read()
        inputStream.close()
        socket.close()
        return data.toString()
    }

    fun solicitar_datos_temperatura(device: BluetoothDevice): String {
        check_bluetooth(device)
        val socket = device.createRfcommSocketToServiceRecord(device.uuids[0].uuid)
        socket.connect()
        val outputStream = socket.outputStream
        outputStream.write('T'.code)
        outputStream.close()
        val inputStream = socket.inputStream
        val data = inputStream.read()
        inputStream.close()
        socket.close()
        return data.toString()
    }

    fun solicitar_datos_luminosidad(device: BluetoothDevice): String {
        check_bluetooth(device)
        val socket = device.createRfcommSocketToServiceRecord(device.uuids[0].uuid)
        socket.connect()
        val outputStream = socket.outputStream
        outputStream.write('L'.code)
        outputStream.close()
        val inputStream = socket.inputStream
        val data = inputStream.read()
        inputStream.close()
        socket.close()
        return data.toString()
    }

}
