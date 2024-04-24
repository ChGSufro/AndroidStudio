// BienvenidaFragment.kt
package com.example.appandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appandroid.R
import com.example.appandroid.api.Api
import java.io.IOException

import com.example.appandroid.app.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class InicioSesion : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sample_inicio_sesion, container, false)

        val usuario = view?.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.Usuario)?.text
        val contraseña = view?.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.Contrasena)?.text

        val alerta = view?.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.Respuesta)

        view.findViewById<Button>(R.id.BotonContinuar).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch() {
                try {
                    val respuesta = SessionManager().IniciarSesion(usuario.toString(), contraseña.toString())
                    withContext(Dispatchers.Main){
                        alerta?.text = respuesta.toString()
                    }
                } catch (error: Exception) {
                    withContext(Dispatchers.Main){
                        alerta?.text = error.message
                    }
                } catch (error: IOException) {
                    withContext(Dispatchers.Main){
                        alerta?.text = "Error de conexión."
                    }
                }
            }
        }

        view.findViewById<Button>(R.id.BotonRegresar).setOnClickListener {
            findNavController().popBackStack()
        }
        return view
    }

}