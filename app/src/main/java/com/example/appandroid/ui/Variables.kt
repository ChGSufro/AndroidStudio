package com.example.appandroid.ui

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appandroid.R

class Variables : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.sample_variables, container, false)
        val USUARIO_ACTIVO = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        val menu: Spinner = view.findViewById(R.id.menu)
        val saludo: TextView = view.findViewById(R.id.saludo)
        val dispConectado: TextView = view.findViewById(R.id.dispConectado)
        val progresoHumedad: ProgressBar = view.findViewById(R.id.progresHumedad)
        val progresoTemperatura: ProgressBar = view.findViewById(R.id.progresTemperatura)
        val progresoLuminosidad: ProgressBar = view.findViewById(R.id.progresLuminosidad)

        // Aquí se crea el menú desplegable
        val opciones = arrayOf("Menú", "Conexión", "Estadística", "Cerrar")
        val adapter_menu = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adapter_menu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        menu.adapter = adapter_menu

        menu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                when (selectedItem) {
                    "Conexión" -> {
                        findNavController().navigate(R.id.Variables_to_Conexion)
                    }
                    "Estadística" -> {
                        // Aquí puedes poner el código que se ejecutará cuando se seleccione "Estadística"
                    }
                    "Cerrar" -> {
                        findNavController().navigate(R.id.Variables_to_Bienvenida)
                        USUARIO_ACTIVO.desconectarUsuario()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        saludo.text = "Bienvenido:\n ${USUARIO_ACTIVO.getUsuario()?.getNombre()}"

        // Aquí se muestran el estado de conexión
        if (USUARIO_ACTIVO.getUsuario()?.getDispConectado() == null) {
            dispConectado.text = "Sin conexión."
        } else{
            val dispositivo: BluetoothDevice = USUARIO_ACTIVO.getUsuario()?.getDispConectado()!!
            dispConectado.text = "Dispositivo conectado: ${dispositivo.name}"
        }

        return view
    }
}