package com.example.appandroid.ui
import androidx.lifecycle.ViewModel
import com.example.appandroid.app.UsuarioActivo

class MyViewModel: ViewModel() {
    private lateinit var usuario: UsuarioActivo

    fun getUsuario(): UsuarioActivo {
        return usuario
    }

    fun setUsuario(usuario: UsuarioActivo) {
        this.usuario = usuario
    }

}