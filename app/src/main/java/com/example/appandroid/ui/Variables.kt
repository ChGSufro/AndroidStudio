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
import com.example.appandroid.app.UsuarioActivo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Variables : Fragment() {

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.sample_variables, container, false)
        val USUARIO_ACTIVO: UsuarioActivo = ViewModelProvider(requireActivity())[MyViewModel::class.java].getUsuario()

        val menu: Spinner = view.findViewById(R.id.menu)
        val saludo: TextView = view.findViewById(R.id.saludo)
        val dispConectado: TextView = view.findViewById(R.id.dispConectado)
        val textHumedad: TextView = view.findViewById(R.id.etiquetaHumedad)
        val textTemperatura: TextView = view.findViewById(R.id.etiquetaTemperatura)
        val textLuminosidad: TextView = view.findViewById(R.id.etiquetaLuminosidad)
        val progresoHumedad: ProgressBar = view.findViewById(R.id.progresHumedad)
        val progresoTemperatura: ProgressBar = view.findViewById(R.id.progresTemperatura)
        val progresoLuminosidad: ProgressBar = view.findViewById(R.id.progresLuminosidad)
        val actualizar: Button = view.findViewById(R.id.botonActualizar)

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
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        saludo.text = "Bienvenido:\n ${USUARIO_ACTIVO.getNombre()}"

        // Aquí se muestran el estado de conexión
        if (USUARIO_ACTIVO.getDispConectado() == null) {
            dispConectado.text = "No hay dispositivo conectado"
        } else{
            dispConectado.text = "Dispositivo conectado: ${USUARIO_ACTIVO.getDispConectado()?.name}"
        }

        // Aquí se actualizan los valores de los sensores
        actualizar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch() {
                try {
                    val HUMEDAD = USUARIO_ACTIVO.obtener_humedad()
                    val TEMPERATURA = USUARIO_ACTIVO.obtener_temperatura()
                    val LUMINOSIDAD = USUARIO_ACTIVO.obtener_luminosidad()

                    withContext(Dispatchers.Main) {
                        textHumedad.text = HUMEDAD.toString()
                        textTemperatura.text = TEMPERATURA.toString()
                        textLuminosidad.text = LUMINOSIDAD.toString()
                        progresoHumedad.progress = HUMEDAD?.toInt() ?: 0
                        progresoTemperatura.progress = TEMPERATURA?.toInt() ?: 0
                        progresoLuminosidad.progress = LUMINOSIDAD?.toInt() ?: 0
                    }

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