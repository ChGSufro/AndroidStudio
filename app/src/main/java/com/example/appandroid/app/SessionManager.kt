package com.example.appandroid.app
import com.example.appandroid.conexiones.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class SessionManager(){

    private val api = Api()
    private val LETRAS_MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val LETRAS_MINUSCULAS = "abcdefghijklmnopqrstuvwxyz"
    private val NUMEROS = "0123456789"
    private val CARACTERES_BLOQUEADOS = " "

    private fun checkUsuario(usuario: String): Boolean {
        if (usuario.contains(CARACTERES_BLOQUEADOS)) {
            throw Exception("El usuario no puede contener espacios.")
        }
        if (usuario.length > 20) {
            throw Exception("El usuario no puede tener más de 20 caracteres.")
        }
        if (usuario.contains(CARACTERES_BLOQUEADOS)){
            throw Exception("El usuario no puede contener espacios.")
        }
        if (usuario.isEmpty()) {
            return false
        }
        return true
    }

    private fun checkNombre(nombre: String): Boolean {
        if (nombre.isEmpty()) {
            return false
        }
        if (nombre.length > 30) {
            throw Exception("El nombre no puede tener más de 30 caracteres.")
        }
        return true
    }

    private fun checkContraseña(contraseña: String): Boolean {
        if (contraseña.contains(CARACTERES_BLOQUEADOS)) {
            throw Exception("La contraseña no puede contener espacios.")
        }
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

    fun IniciarSesion(usuario: String, contraseña: String): JsonObject {
        if (!checkUsuario(usuario) || !checkContraseña(contraseña)){
            throw Exception("Rellene todos los campos.")
        }
        val jsonUsuario = JsonObject(mapOf("rut" to JsonPrimitive(usuario), "contraseña" to JsonPrimitive(contraseña)))
        val respuesta = api.log_usuario(jsonUsuario)
        if (respuesta["respuesta"].toString() == "Usuario no encontrado.") {
            throw Exception("Usuario no encontrado.")
        }
        return respuesta["respuesta"] as JsonObject
    }

    fun RegistrarUsuario(usuario: String, nombre: String, contraseña: String, confcontraseña: String): String {
        if (!checkUsuario(usuario) || !checkNombre(nombre) || !checkContraseñas(contraseña, confcontraseña)){
            throw Exception("Rellene todos los campos.")
        }
        val jsonUsuarioNuevo = JsonObject(mapOf(
            "rut" to JsonPrimitive(usuario),
            "nombre" to JsonPrimitive(nombre),
            "contraseña" to JsonPrimitive(contraseña)))
        val respuesta = api.add_usuario(jsonUsuarioNuevo)
        return respuesta["respuesta"].toString()
    }

}
