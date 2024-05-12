package com.example.appandroid.app

import android.bluetooth.BluetoothDevice
import com.example.appandroid.conexiones.Api
import com.example.appandroid.conexiones.Bluetooth
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray

class UsuarioActivo (usuario: String, nombre: String) {

    private val Api: Api = Api()
    private val Bluetooth: Bluetooth = Bluetooth()
    private var disp_conectado: BluetoothDevice? = null

    private var usuario: String = usuario
    private var nombre: String = nombre

    fun getUsuario(): String {
        return usuario
    }

    fun getNombre(): String {
        return nombre
    }

    fun getDispConectado(): BluetoothDevice? {
        return disp_conectado
    }

    fun setDispConectado(disp: BluetoothDevice) {
        disp_conectado = disp
    }


    //metodos de gestion de datos arduino
    fun obtener_humedad(): String? {
        val HUMEDAD = disp_conectado?.let { Bluetooth.solicitar_datos_humedad(it) }
        return HUMEDAD
    }

    fun obtener_temperatura(): String? {
        val TEMPERATURA = disp_conectado?.let { Bluetooth.solicitar_datos_temperatura(it) }
        return TEMPERATURA
    }

    fun obtener_luminosidad(): String? {
        val LUMINOSIDAD = disp_conectado?.let { Bluetooth.solicitar_datos_luminosidad(it) }
        return LUMINOSIDAD
    }
}