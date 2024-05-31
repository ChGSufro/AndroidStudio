package com.example.appandroid.app

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.appandroid.datos.Api
import com.example.appandroid.datos.Bluetooth
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@RequiresApi(Build.VERSION_CODES.O)
class UsuarioActivo (usuario: String, nombre: String, humedad: Float, temperatura: Float, luminosidad: Float) {

    private val Api: Api = Api()
    private val Bluetooth: Bluetooth = Bluetooth()
    private var disp_conectado: BluetoothDevice? = null
    private var coneccion: BluetoothSocket? = null
    val cropManager: CropManager = CropManager(this)

    private var usuario: String = usuario.replace("\"", "")
    private var nombre: String = nombre.replace("\"", "")
    private var humedad: Float = humedad
    private var temperatura: Float = temperatura
    private var luminosidad: Float = luminosidad
    private var hora_ultima_accion: Long = 0

    fun getNombre(): String {
        return nombre
    }

    fun getDispConectado(): BluetoothDevice? {
        return disp_conectado
    }

    fun getDatosPromediadosFecha(): List<JsonObject> {
        return cropManager.agrupar_por_fecha(descargarDatosDB())
    }

    fun getHumedad(): Float {
        return humedad
    }

    fun getTemperatura(): Float {
        return temperatura
    }

    fun getLuminosidad(): Float {
        return luminosidad
    }

    fun humedad_correcta(valor: Float) {
        if (valor < 0 || valor > 100) {
            throw Exception("Valor de humedad fuera de rango")
        }
        this.humedad = valor
    }

    fun temperatura_correcta(valor: Float) {
        if (valor < 0) {
            throw Exception("Valor de temperatura fuera de rango")
        }
        this.temperatura = valor
    }

    fun luminosidad_correcta(valor: Float){
        if (valor < 0) {
            throw Exception("Valor de luminosidad fuera de rango")
        }
        this.luminosidad = valor
    }

    //metodos de gestion Bluetooth

    fun conectarDisp(nombre_dispositivo: String) {
        disp_conectado = Bluetooth.obtener_dispositivo_por_nombre(nombre_dispositivo)
        coneccion = Bluetooth.conectar(disp_conectado!!)
    }

    fun desconectarDisp() {
        if (coneccion == null) {
            throw Exception("No hay dispositivo conectado")
        }
        Bluetooth.desconectar(coneccion!!)
    }

    //metodos de gestion de datos BASE DE DATOS

    fun descargarDatosDB(): List<JsonObject> {
        val datos = Api.get_datos_cultivos(usuario)
        return datos["respuesta"] as List<JsonObject>
    }

    fun subirDatosDB(cultivo: JsonObject): JsonObject {
        val respuesta = Api.add_cultivo(usuario, cultivo)
        return respuesta
    }

    fun actualizarDatosDB(humedad: Float, temperatura: Float, luminosidad: Float){
        if (humedad == this.humedad && temperatura == this.temperatura && luminosidad == this.luminosidad) {
            throw Exception("No se han modificado los datos")
        }
        humedad_correcta(humedad)
        temperatura_correcta(temperatura)
        luminosidad_correcta(luminosidad)
        val datos = JsonObject(mapOf(
            "usuario" to JsonPrimitive(usuario),
            "humedad" to JsonPrimitive(humedad),
            "temperatura" to JsonPrimitive(temperatura),
            "luz" to JsonPrimitive(luminosidad)))
        Api.update_usuario(datos)
    }


    //metodos de gestion de datos arduino
    fun obtener_humedad(): Float {
        try {
            Bluetooth.check_bluetooth()
            Bluetooth.enviar_char(coneccion!!, 'H')
            val dato = Bluetooth.recibir_dato(coneccion!!)
            cropManager.agregar_humedad(dato)
            return dato

        } catch (e: NullPointerException) {
            throw Exception("No hay dispositivo conectado")
        }
    }

    fun obtener_temperatura(): Float {
        try {
            Bluetooth.check_bluetooth()
            Bluetooth.enviar_char(coneccion!!, 'T')
            val dato = Bluetooth.recibir_dato(coneccion!!)
            cropManager.agregar_temperatura(dato)
            return dato

        } catch (e: NullPointerException) {
            throw Exception("No hay dispositivo conectado")
        }
    }

    fun obtener_luminosidad(): Float {
        try {
            Bluetooth.check_bluetooth()
            Bluetooth.enviar_char(coneccion!!, 'L')
            val dato = Bluetooth.recibir_dato(coneccion!!)
            cropManager.agregar_luminosidad(dato)
            return dato

        } catch (e: NullPointerException) {
            throw Exception("No hay dispositivo conectado")
        }
    }

    fun subir_techo(){
        try {
            if (hora_ultima_accion + 5000 > System.currentTimeMillis()) {
                throw Exception("Espere 5 segundos antes de realizar otra accion")
            }
            Bluetooth.check_bluetooth()
            Bluetooth.enviar_char(coneccion!!, 'S')
            hora_ultima_accion = System.currentTimeMillis()
        } catch (e: NullPointerException) {
            throw Exception("No hay dispositivo conectado")
        }

    }

    fun bajar_techo(){
        try {
            if (hora_ultima_accion + 5000 > System.currentTimeMillis()) {
                throw Exception("Espere 5 segundos antes de realizar otra accion")
            }
            Bluetooth.check_bluetooth()
            Bluetooth.enviar_char(coneccion!!, 'B')
            hora_ultima_accion = System.currentTimeMillis()
        } catch (e: NullPointerException) {
            throw Exception("No hay dispositivo conectado")
        }
    }


}