package com.example.appandroid.conexiones

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice


class Bluetooth {

    private val BLUETOOTH_ADAPTER = BluetoothAdapter.getDefaultAdapter()

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

}