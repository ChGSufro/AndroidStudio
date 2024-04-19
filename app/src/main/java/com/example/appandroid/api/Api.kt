package com.example.appandroid.api
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

    fun log_usuario(usuario: JsonObject): JsonObject{
        val usr = formatJson_toRequestBody(usuario)
        val request = Request.Builder().url("$URL_IP/usuarios/log").post(usr).build()
        return formatResponse_toJson(cliente.newCall(request).execute())
    }

    fun add_usuario(usuario: JsonObject): JsonObject{
        val usr = formatJson_toRequestBody(usuario)
        val request = Request.Builder().url("$URL_IP/usuarios/add").post(usr).build()
        return formatResponse_toJson(cliente.newCall(request).execute())
    }
}