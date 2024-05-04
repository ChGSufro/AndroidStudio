package com.example.appandroid.ui
import androidx.lifecycle.ViewModel
import com.example.appandroid.app.UsuarioActivo

class MyViewModel: ViewModel() {
    private var usuario: UsuarioActivo? = null

    fun getUsuario(): UsuarioActivo? {
        return usuario
    }

    fun setUsuario(usuario: UsuarioActivo) {
        this.usuario = usuario
    }

    fun desconectarUsuario() {
        this.usuario = null
    }
}