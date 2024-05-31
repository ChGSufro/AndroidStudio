package com.example.appandroid.datos

import android.bluetooth.*
import okio.ByteString.Companion.readByteString

class Bluetooth {

    private val BLUETOOTH_ADAPTER = BluetoothAdapter.getDefaultAdapter()

    fun check_bluetooth() {
        if (!BLUETOOTH_ADAPTER.isEnabled) {
            throw Exception("Bluetooth apagado")
        }
    }

    fun obtener_dispositivos_vinculados(): List<BluetoothDevice> {
        try {
            check_bluetooth()
            return BLUETOOTH_ADAPTER.bondedDevices.toList()
        } catch (e: SecurityException) {
            throw Exception("Revise los permisos de Bluetooth")
        }
    }

    fun obtener_dispositivo_por_nombre(nombre: String): BluetoothDevice? {
        try {
            check_bluetooth()
            for (device in BLUETOOTH_ADAPTER.bondedDevices) {
                if (device.name == nombre) {
                    return device
                }
            }
        } catch (e: SecurityException) {
            throw Exception("Problemas al conectar el dispositivo")
        }
        throw Exception("No se encontró el dispositivo")
    }

    fun conectar(device: BluetoothDevice) : BluetoothSocket {
        check_bluetooth()
        try {
            val socket = device.createRfcommSocketToServiceRecord(device.uuids[0].uuid)
            socket.connect()
            return socket
        } catch (e: SecurityException) {
            throw Exception("Revise los permisos de Bluetooth")
        } catch (e: Exception) {
            throw Exception("Dispositivo no disponible.")
        }
    }

    fun desconectar(socket: BluetoothSocket) {
        socket.close()
    }

    fun enviar_char(socket: BluetoothSocket, char: Char) {
        check_bluetooth()
        val outputStream = socket.outputStream
        outputStream.write(char.code)
    }

    fun recibir_dato(socket: BluetoothSocket): Float {
        val inputStream = socket.inputStream
        val data = inputStream.bufferedReader() // Leer una línea de la entrada
        return data.readLine().toFloat() // Convertir la cadena a Float
    }

}
