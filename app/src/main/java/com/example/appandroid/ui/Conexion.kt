package com.example.appandroid.ui

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appandroid.R
import com.example.appandroid.conexiones.Bluetooth as BT

class Conexion() : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.sample_conexion, container, false)
        val USUARIO_ACTIVO = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        val menu: Spinner = view.findViewById(R.id.menu)
        val listaEmparejados = view.findViewById<ListView>(R.id.listaDispEmparejados)
        val actualizar = view.findViewById<View>(R.id.botonEmparejar)

        // Aquí se crea el menú desplegable
        val opciones_menu = arrayOf("Menú", "Variables", "Estadística", "Cerrar")
        val adapter_menu = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones_menu)
        adapter_menu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        menu.adapter = adapter_menu

        menu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                when (selectedItem) {
                    "Variables" -> {
                        findNavController().navigate(R.id.Conexión_to_Variables)
                    }
                    "Estadística" -> {
                        // Aquí puedes poner el código que se ejecutará cuando se seleccione "Estadística"
                    }
                    "Cerrar" -> {
                        findNavController().navigate(R.id.Conexión_to_Bienvenida)
                        USUARIO_ACTIVO.desconectarUsuario()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        // Aquí se obtienen los dispositivos vinculados y se muestran en la lista
        fun cargarDispositivos() {
            val dispositivos = BT().obtener_dispositivos_vinculados()
            val nombresDispositivos = dispositivos.map { it.name }.toTypedArray()
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_selectable_list_item, nombresDispositivos)
            listaEmparejados.adapter = adapter
        }

        cargarDispositivos()

        listaEmparejados.setOnItemClickListener { _, _, position, _ ->
            val dispositivo = listaEmparejados.getItemAtPosition(position).toString()
            val dispositivoSeleccionado: BluetoothDevice? = BT().obtener_dispositivo_por_nombre(dispositivo)
            if (dispositivoSeleccionado != null) {
                USUARIO_ACTIVO.getUsuario()?.setDispConectado(dispositivoSeleccionado)
                findNavController().navigate(R.id.Conexión_to_Variables)
                Toast.makeText(context, "Conectado a $dispositivo", Toast.LENGTH_SHORT).show()
            }
        }

        // Aquí se actualiza la lista de dispositivos emparejados
        actualizar.setOnClickListener {
            cargarDispositivos()
        }

        return view
    }
}