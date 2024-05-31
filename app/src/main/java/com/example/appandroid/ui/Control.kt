// BienvenidaFragment.kt
package com.example.appandroid.ui

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appandroid.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Control : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        val view = inflater.inflate(R.layout.sample_control, container, false)
        val USUARIO_ACTIVO = ViewModelProvider(requireActivity())[MyViewModel::class.java].getUsuario()
        val menu: Spinner = view.findViewById(R.id.menu)
        val botonSubir: Button = view.findViewById(R.id.botonSubir)
        val botonBajar: Button = view.findViewById(R.id.botonBajar)

        // Aquí se crea el menú desplegable
        val opciones_menu = arrayOf("Menú", "Variables", "Conexión", "Estadística", "Cerrar")
        val adapter_menu = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones_menu)
        adapter_menu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        menu.adapter = adapter_menu

        menu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                when (selectedItem) {
                    "Variables" -> {
                        findNavController().navigate(R.id.Control_to_Variables)
                    }

                    "Conexión" -> {
                        findNavController().navigate(R.id.Control_to_Conexion)
                    }

                    "Estadística" -> {
                        findNavController().navigate(R.id.Control_to_Estadistica)
                    }

                    "Cerrar" -> {
                        findNavController().navigate(R.id.Control_to_Bienvenida)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        botonSubir.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    USUARIO_ACTIVO.subir_techo()
                } catch (e: Exception) {
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        botonBajar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    USUARIO_ACTIVO.bajar_techo()
                } catch (e: Exception) {
                    withContext(Dispatchers.Main){
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return view
    }

}