// BienvenidaFragment.kt
package com.example.appandroid.ui

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appandroid.R
import com.example.appandroid.app.UsuarioActivo
import com.example.appandroid.app.SessionManager as SM
import kotlinx.coroutines.*

class InicioSesion : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        val view = inflater.inflate(R.layout.sample_inicio_sesion, container, false)
        val USUARIO_ACTIVO = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        val usuario: EditText = view.findViewById(R.id.Usuario)
        val contraseña: EditText = view.findViewById(R.id.Contrasena)
        val alerta: TextView = view.findViewById(R.id.Respuesta)

        view.findViewById<Button>(R.id.BotonContinuar).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val respuesta = SM().IniciarSesion(usuario.text.toString(), contraseña.text.toString())
                    withContext(Dispatchers.Main) {
                        val new_usuario = UsuarioActivo(respuesta?.get("rut")?.toString() ?: "", respuesta?.get("nombre")?.toString() ?: "")
                        USUARIO_ACTIVO.setUsuario(new_usuario)
                        findNavController().navigate(R.id.InicioSesion_to_log)
                        alerta?.text = respuesta.toString()
                    }
                } catch (error: Exception) {
                    withContext(Dispatchers.Main) {
                        alerta?.text = error.message
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