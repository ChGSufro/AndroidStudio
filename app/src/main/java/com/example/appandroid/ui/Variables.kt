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
import com.example.appandroid.app.UsuarioActivo
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

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
        val progresoHumedad: PieChart = view.findViewById(R.id.progresHumedad)
        val progresoTemperatura: PieChart = view.findViewById(R.id.progresTemperatura)
        val progresoLuminosidad: PieChart = view.findViewById(R.id.progresLuminosidad)
        val actualizar: Button = view.findViewById(R.id.botonActualizar)
        val detener: Button = view.findViewById(R.id.botonDetener)
        val humedadRecomendada: EditText = view.findViewById(R.id.humedadRecomendadaValor)
        val temperaturaRecomendada: EditText = view.findViewById(R.id.temperaturaRecomendadaValor)
        val luminosidadRecomendada: EditText = view.findViewById(R.id.luminosidadRecomendadaValor)
        val botonGuardar: Button = view.findViewById(R.id.botonGuardarDatos)

        var estaActualizando = false

        // Aquí se crea el menú desplegable
        val opciones = arrayOf("Menú", "Conexión", "Control", "Estadística", "Cerrar")
        val adapter_menu =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adapter_menu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        menu.adapter = adapter_menu

        definir_grafico(progresoHumedad)
        definir_grafico(progresoTemperatura)
        definir_grafico(progresoLuminosidad)

        menu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                when (selectedItem) {
                    "Conexión" -> {
                        findNavController().navigate(R.id.Variables_to_Conexion)
                    }
                    "Control" -> {
                        findNavController().navigate(R.id.Variables_to_Control)
                    }
                    "Estadística" -> {
                        findNavController().navigate(R.id.Variables_to_Estadistica)
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
        } else {
            dispConectado.text = "Dispositivo conectado: ${USUARIO_ACTIVO.getDispConectado()?.name}"
        }

        // Aquí se guardan los valores recomendados
        humedadRecomendada.setText(USUARIO_ACTIVO.getHumedad().toString())
        humedadRecomendada.setHint(USUARIO_ACTIVO.getHumedad().toString())
        temperaturaRecomendada.setText(USUARIO_ACTIVO.getTemperatura().toString())
        temperaturaRecomendada.setHint(USUARIO_ACTIVO.getTemperatura().toString())
        luminosidadRecomendada.setText(USUARIO_ACTIVO.getLuminosidad().toString())
        luminosidadRecomendada.setHint(USUARIO_ACTIVO.getLuminosidad().toString())

        botonGuardar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    USUARIO_ACTIVO.actualizarDatosDB(
                        humedadRecomendada.text.toString().toFloat(),
                        temperaturaRecomendada.text.toString().toFloat(),
                        luminosidadRecomendada.text.toString().toFloat()
                    )
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Datos guardados", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Aquí se actualizan los valores de los sensores
        actualizar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                estaActualizando = true
                while (estaActualizando) {
                    try {
                        var HUMEDAD = USUARIO_ACTIVO.obtener_humedad()
                        var TEMPERATURA = USUARIO_ACTIVO.obtener_temperatura()
                        var LUMINOSIDAD = USUARIO_ACTIVO.obtener_luminosidad()
                        withContext(Dispatchers.Main) {

                            textHumedad.text = HUMEDAD.toString() + " %"
                            textTemperatura.text = TEMPERATURA.toString() + " °C"
                            textLuminosidad.text = LUMINOSIDAD.toString() + " lx"

                            if (HUMEDAD is Float) {
                                val porcHum = USUARIO_ACTIVO.cropManager.porcentaje(HUMEDAD, USUARIO_ACTIVO.getHumedad())
                                if (porcHum > 100) {
                                    set_porcentaje_grafico(100f, progresoHumedad, R.color.Rojo_oscuro)
                                } else {
                                    set_porcentaje_grafico(porcHum, progresoHumedad, R.color.humedad)
                                }
                            }

                            if (TEMPERATURA is Float) {
                                val porcTemp = USUARIO_ACTIVO.cropManager.porcentaje(TEMPERATURA, USUARIO_ACTIVO.getTemperatura())
                                if (porcTemp > 100) {
                                    set_porcentaje_grafico(
                                        100f,
                                        progresoTemperatura,
                                        R.color.Rojo_oscuro
                                    )
                                } else {
                                    set_porcentaje_grafico(
                                        porcTemp,
                                        progresoTemperatura,
                                        R.color.temperatura
                                    )
                                }
                            }

                            if (LUMINOSIDAD is Float) {
                                val porcLum = USUARIO_ACTIVO.cropManager.porcentaje(LUMINOSIDAD, USUARIO_ACTIVO.getLuminosidad())
                                if (porcLum > 100) {
                                    set_porcentaje_grafico(
                                        100f,
                                        progresoLuminosidad,
                                        R.color.Rojo_oscuro
                                    )
                                } else {
                                    set_porcentaje_grafico(
                                        porcLum,
                                        progresoLuminosidad,
                                        R.color.luminosidad
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                            estaActualizando= false
                        }
                    }
                    delay(2000)
                }
            }
        }

        detener.setOnClickListener {
            estaActualizando = false
        }


        return view
    }

    fun set_porcentaje_grafico(valor: Float, grafico: PieChart, color: Int){
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(100f - valor, ""))//valor faltante
        entries.add(PieEntry(valor, ""))//valor a llenar

        val dataSet = PieDataSet(entries, "")
        dataSet.setDrawValues(false)

        val colores = ArrayList<Int>()
        colores.add(resources.getColor(R.color.white))
        colores.add(resources.getColor(color))
        dataSet.colors = colores

        val data = PieData(dataSet)
        grafico.data = data
        grafico.invalidate()
    }

    fun definir_grafico(grafico: PieChart){
        grafico.description.isEnabled = false // Desactivar la descripción
        grafico.legend.isEnabled = false // Desactivar la etiqueta
        grafico.holeRadius = 78f // Tamaño del agujero en el centro
        grafico.transparentCircleRadius = 78f // Tamaño del círculo transparente alrededor del agujero
        grafico.invalidate() // Refrescar el gráfico
    }
}