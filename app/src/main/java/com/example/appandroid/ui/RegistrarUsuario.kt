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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException

import com.example.appandroid.app.SessionManager as SM

/**
 * TODO: document your custom view class.
 */
class RegistrarUsuario : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sample_registrar_usuario, container, false)

        val usuario = view?.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.Usuario)?.text
        val nombre = view?.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.Nombre)?.text
        val contraseña = view?.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.Contraseña)?.text
        val confcontraseña = view?.findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.ConfirmarContraseña)?.text

        val alerta = view?.findViewById<androidx.appcompat.widget.AppCompatTextView>(R.id.Respuesta)

        view.findViewById<Button>(R.id.BotonRegistrar).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch() {
                try {
                    val response = SM().RegistrarUsuario(usuario.toString(), nombre.toString(), contraseña.toString(), confcontraseña.toString())
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show()
                        alerta?.text = response
                        if (response == "Usuario agregado."){
                            findNavController().popBackStack()
                        }
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