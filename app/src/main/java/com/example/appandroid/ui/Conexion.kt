package com.example.appandroid.ui

import android.bluetooth.BluetoothDevice
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appandroid.R
import kotlinx.coroutines.*
import com.example.appandroid.datos.Bluetooth as BT

class Conexion() : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.sample_conexion, container, false)
        val USUARIO_ACTIVO = ViewModelProvider(requireActivity())[MyViewModel::class.java].getUsuario()

        val menu: Spinner = view.findViewById(R.id.menu)
        val listaEmparejados = view.findViewById<ListView>(R.id.listaDispEmparejados)
        val actualizar = view.findViewById<View>(R.id.botonEmparejar)
        val desemparejar = view.findViewById<View>(R.id.botonDesemparejar)

        // Aquí se crea el menú desplegable
        val opciones_menu = arrayOf("Menú", "Variables", "Control", "Estadística", "Cerrar")
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
                    "Control" -> {
                        findNavController().navigate(R.id.Conexión_to_Control)
                    }
                    "Estadística" -> {
                        findNavController().navigate(R.id.Conexión_to_Estadistica)
                    }
                    "Cerrar" -> {
                        findNavController().navigate(R.id.Conexión_to_Bienvenida)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        // Aquí se obtienen los dispositivos vinculados y se muestran en la lista
        fun cargarDispositivos() {
            try {
                val dispositivos: List<BluetoothDevice> = BT().obtener_dispositivos_vinculados()
                val nombresDispositivos = dispositivos.map { it.name }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_selectable_list_item, nombresDispositivos)
                listaEmparejados.adapter = adapter
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }

        }

        cargarDispositivos()

        listaEmparejados.setOnItemClickListener { _, _, position, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val dispositivo = listaEmparejados.getItemAtPosition(position).toString()
                    USUARIO_ACTIVO.conectarDisp(dispositivo)
                    withContext(Dispatchers.Main){
                        findNavController().navigate(R.id.Conexión_to_Variables)
                        Toast.makeText(context, "Conectado a $dispositivo", Toast.LENGTH_SHORT).show()
                    }
                }catch (e: Exception) {
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Aquí se actualiza la lista de dispositivos emparejados
        actualizar.setOnClickListener {
            cargarDispositivos()
        }

        // Aquí se desvincula el dispositivo seleccionado
        desemparejar.setOnClickListener {
            try {
                USUARIO_ACTIVO.desconectarDisp()
                Toast.makeText(context, "Desconectado", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}