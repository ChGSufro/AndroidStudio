package com.example.appandroid.datos

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class Api {
    private val cliente = OkHttpClient()
    private val URL_IP: String = "http://44.209.21.223:8081"

    private fun formatJson_toRequestBody(json: JsonObject): RequestBody {
        val jsonString: String = json.toString()
        val jsonMediaType = "application/json; charset=utf-8".toMediaType()
        return jsonString.toRequestBody(jsonMediaType)
    }

    private fun formatResponse_toJson(response: Response): JsonObject {
        val jsonString: String = response.body?.string() ?: ""
        return Json.parseToJsonElement(jsonString) as JsonObject
    }

    fun log_usuario(usuario: JsonObject) : JsonObject{
        val usr = formatJson_toRequestBody(usuario)
        val request = Request.Builder().url("$URL_IP/usuarios/log").post(usr).build()
        val response = cliente.newCall(request).execute()
        return formatResponse_toJson(response)
    }

    fun add_usuario(usuario: JsonObject): JsonObject {
        val usr = formatJson_toRequestBody(usuario)
        val request = Request.Builder().url("$URL_IP/usuarios/add").put(usr).build()
        val response = cliente.newCall(request).execute()
        return formatResponse_toJson(response)
    }

    fun update_usuario(usuario: JsonObject) {
        val usr = formatJson_toRequestBody(usuario)
        val request = Request.Builder().url("$URL_IP/usuarios/update").put(usr).build()
        val response = cliente.newCall(request).execute()
    }

    //return["respuesta"] = List<JsonObject>
    fun get_nombres_cultivos(usuario: String): JsonObject {
        val request = Request.Builder().url("$URL_IP/get/coleccion/nombres/<$usuario>").get().build()
        val response = cliente.newCall(request).execute()
        return formatResponse_toJson(response)
    }
    fun get_datos_cultivos(usuario: String): JsonObject {
        val request = Request.Builder().url("$URL_IP/get/coleccion/$usuario/cultivo1").get().build()
        val response = cliente.newCall(request).execute()
        return formatResponse_toJson(response)
    }
    fun add_cultivo(usuario: String, cultivo: JsonObject): JsonObject {
        val cult = formatJson_toRequestBody(cultivo)
        val request = Request.Builder().url("$URL_IP/add/coleccion/$usuario").put(cult).build()
        val response = cliente.newCall(request).execute()
        return formatResponse_toJson(response)
    }
    fun delete_cultivo(usuario: String, cultivo: String): JsonObject {
        val request = Request.Builder().url("$URL_IP/delete/coleccion/$usuario/${cultivo}").delete().build()
        val response = cliente.newCall(request).execute()
        return formatResponse_toJson(response)
    }
}