package com.example.appandroid.app
import com.example.appandroid.api.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import okhttp3.Dispatcher

class SessionManager(){

    val api = Api()

    private fun checkUsuario(usuario: String): Boolean {
        usuario.replace(" ", "")
        if (usuario.isEmpty()) {
            return false
        }
        return true
    }

    private fun checkNombre(nombre: String): Boolean {
        nombre.replace(" ", "")
        if (nombre.isEmpty()) {
            return false
        }
        return true
    }

    private fun checkContraseña(contraseña: String): Boolean {
        contraseña.replace(" ", "")
        if (contraseña.isEmpty()) {
            return false
        }
        return true
    }

    private fun checkContraseñas(contraseña: String, confcontraseña: String): Boolean {
        if (contraseña != confcontraseña) {
            throw Exception("Las contraseñas no coinciden.")
        }
        confcontraseña.replace(" ", "")
        if (confcontraseña.isEmpty()) {
            return false
        }
        return true
    }

    suspend fun IniciarSesion(usuario: String, contraseña: String): JsonObject? {
        if (!checkUsuario(usuario) || !checkContraseña(contraseña)){
            throw Exception("Rellene todos los campos.")
        }
        val jsonUsuario = JsonObject(mapOf("rut" to JsonPrimitive(usuario), "contraseña" to JsonPrimitive(contraseña)))
        val respuesta = CoroutineScope(Dispatchers.IO).async { api.log_usuario(jsonUsuario) }.await()
        return respuesta["respuesta"] as? JsonObject
    }

    suspend fun RegistrarUsuario(usuario: String, nombre: String, contraseña: String, confcontraseña: String): String{
        if (!checkUsuario(usuario) || !checkNombre(nombre) || !checkContraseñas(contraseña, confcontraseña)){
            throw Exception("Rellene todos los campos.")
        }
        val jsonUsuarioNuevo = JsonObject(mapOf(
            "rut" to JsonPrimitive(usuario),
            "nombre" to JsonPrimitive(nombre),
            "contraseña" to JsonPrimitive(contraseña)))
        val respuesta = CoroutineScope(Dispatchers.IO).async { api.add_usuario(jsonUsuarioNuevo) }.await()
        return respuesta["respuesta"].toString()
    }

}
