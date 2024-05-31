package com.example.appandroid.main
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.appandroid.app.UsuarioActivo
import com.example.appandroid.datos.Api
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive


@RequiresApi(Build.VERSION_CODES.O)
fun main(){

    val api = Api()

    val cultivo = JsonObject(mapOf(
        "nombre" to JsonPrimitive("cultivo1"),
        "fecha" to JsonPrimitive("01/06/2023"),
        "luminosidad" to JsonPrimitive(30.0f),
        "temperatura" to JsonPrimitive(42.0f),
        "humedad" to JsonPrimitive(32.0f)
    ))

    val cultivo2 = JsonObject(mapOf(
        "nombre" to JsonPrimitive("cultivo1"),
        "fecha" to JsonPrimitive("01/05/2023"),
        "luminosidad" to JsonPrimitive(40.0f),
        "temperatura" to JsonPrimitive(72.0f),
        "humedad" to JsonPrimitive(22.0f)
    ))

    val cultivo3 = JsonObject(mapOf(
        "nombre" to JsonPrimitive("cultivo1"),
        "fecha" to JsonPrimitive("01/05/2023"),
        "luminosidad" to JsonPrimitive(50.0f),
        "temperatura" to JsonPrimitive(52.0f),
        "humedad" to JsonPrimitive(42.0f)
    ))


    val prueba: ArrayList<JsonObject> = arrayListOf(cultivo, cultivo2, cultivo2, cultivo3)

    val respuesta = api.get_datos_cultivos("212901075")["respuesta"] as List<JsonObject>

}