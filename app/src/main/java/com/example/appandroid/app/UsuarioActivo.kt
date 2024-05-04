package com.example.appandroid.app

import android.bluetooth.BluetoothDevice

class UsuarioActivo (usuario: String, nombre: String) {

    private var usuario: String = usuario
    private var nombre: String = nombre
    private var disp_conectado: BluetoothDevice? = null

    fun getUsuario(): String {
        return usuario
    }

    fun getNombre(): String {
        return nombre
    }

    fun getDispConectado(): BluetoothDevice? {
        return disp_conectado
    }

    fun setUsuario(usuario: String, nombre: String) {
        this.usuario = usuario
        this.nombre = nombre
    }

    fun setDispConectado(disp: BluetoothDevice) {
        disp_conectado = disp
    }

    fun desconectarDisp() {
        disp_conectado = null
    }
}