package com.example.appandroid.app

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class CropManager(usuario: UsuarioActivo) {
    var LUMINOSIDAD: ArrayList<Float> = ArrayList()
    var TEMPERATURA: ArrayList<Float> = ArrayList()
    var HUMEDAD: ArrayList<Float> = ArrayList()

    val USUARIO_ACTIVO = usuario

    fun fecha_actual(): String {
        val fecha = LocalTime.now().toString()
        val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return fecha.format(formato)
    }

    fun agregar_luminosidad(valor: Float?) {
        if (valor is Float) {
            LUMINOSIDAD.add(valor)
            subir_datos()
        }
    }

    fun agregar_temperatura(valor: Float?) {
        if (valor is Float) {
            TEMPERATURA.add(valor)
            subir_datos()
        }
    }

    fun agregar_humedad(valor: Float?) {
        if (valor is Float) {
            HUMEDAD.add(valor)
            subir_datos()
        }
    }

    fun subir_datos() {
        if (LUMINOSIDAD.size > 10 && TEMPERATURA.size > 10 && HUMEDAD.size > 10) {
            val cultivo = JsonObject(mapOf(
                "nombre" to JsonPrimitive("cultivo1"),
                "fecha" to JsonPrimitive(fecha_actual()),
                "luminosidad" to JsonPrimitive(String.format("%.2f", promedio(LUMINOSIDAD)).toFloat()),
                "temperatura" to JsonPrimitive(String.format("%.2f", promedio(TEMPERATURA)).toFloat()),
                "humedad" to JsonPrimitive(String.format("%.2f", promedio(HUMEDAD)).toFloat())
            ))
            USUARIO_ACTIVO.subirDatosDB(cultivo)
        }
        LUMINOSIDAD.clear()
        TEMPERATURA = ArrayList()
        HUMEDAD = ArrayList()
    }

    fun promedio(datos: ArrayList<Float>): Float {
        var suma = 0.0f
        for (dato in datos) {
            suma += dato
        }
        return suma / datos.size
    }


    //Metodos de estadistica
    private fun sacar_promedio(cultivos: ArrayList<JsonObject>): JsonObject {
        var luminosidad = 0.0f
        var temperatura = 0.0f
        var humedad = 0.0f
        for (cultivo in cultivos) {
            luminosidad += cultivo["luminosidad"].toString().toFloat()
            temperatura += cultivo["temperatura"].toString().toFloat()
            humedad += cultivo["humedad"].toString().toFloat()
        }
        luminosidad /= cultivos.size
        temperatura /= cultivos.size
        humedad /= cultivos.size
        return JsonObject(mapOf(
            "nombre" to JsonPrimitive("cultivo1"),
            "fecha" to JsonPrimitive("01/05/2023"),
            "luminosidad" to JsonPrimitive(luminosidad),
            "temperatura" to JsonPrimitive(temperatura),
            "humedad" to JsonPrimitive(humedad)
        ))
    }

    fun agrupar_por_fecha(cultivos: List<JsonObject>): List<JsonObject> {
        var fecha = ""
        var lista_datos_igual_fecha: ArrayList<JsonObject> = arrayListOf()
        val lista_resultante = arrayListOf<JsonObject>()
        if (cultivos.size == 0 || cultivos.size == 1) {
            return cultivos }

        for (cultivo in cultivos) {
            if (cultivo["fecha"].toString() == fecha) {
                lista_datos_igual_fecha.add(cultivo)
            }
            if (cultivo["fecha"].toString() != fecha) {
                fecha = cultivo["fecha"].toString()
                if (lista_datos_igual_fecha.size == 1) {
                    lista_resultante.add(lista_datos_igual_fecha[0])
                }

                if (lista_datos_igual_fecha.size > 1) {
                    lista_resultante.add(sacar_promedio(lista_datos_igual_fecha))
                }
                lista_datos_igual_fecha = arrayListOf()
                lista_datos_igual_fecha.add(cultivo)
            }
            if (cultivo == cultivos.last()) {
                if (lista_datos_igual_fecha.size == 1) {
                    lista_resultante.add(lista_datos_igual_fecha[0])
                }
                if (lista_datos_igual_fecha.size > 1) {
                    lista_resultante.add(sacar_promedio(lista_datos_igual_fecha))
                }
            }
        }
        return lista_resultante
    }

    fun porcentaje(valor_obtenido: Float, valor_maximo: Float): Float {
        val porcentaje = (valor_obtenido / valor_maximo) * 100
        if (porcentaje > 100) {
            return 100.0f
        }
        return porcentaje
    }
}