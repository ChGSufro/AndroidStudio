package com.example.appandroid.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.appandroid.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class Estadistica : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val USUARIO_ACTIVO = ViewModelProvider(requireActivity())[MyViewModel::class.java].getUsuario()

        val view = inflater.inflate(R.layout.sample_estadistica, container, false)
        val menu: Spinner = view.findViewById(R.id.menu)
        val graficoHumedad: LineChart = view.findViewById(R.id.garfico_humedad)
        val graficaTemperatura: LineChart = view.findViewById(R.id.grafico_temperatura)
        val graficoLuminosidad: LineChart = view.findViewById(R.id.grafico_luminosidad)

        // Aquí se crea el menú desplegable
        val opciones = arrayOf("Menú", "Conexión", "Control", "Variables", "Cerrar")
        val adapter_menu = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adapter_menu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        menu.adapter = adapter_menu

        menu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                when (selectedItem) {
                    "Conexión" -> {
                        findNavController().navigate(R.id.Estadistica_to_Conexion)
                    }
                    "Control" -> {
                        findNavController().navigate(R.id.Estadistica_to_Control)
                    }
                    "Variables" -> {
                        findNavController().navigate(R.id.Estadistica_to_Variables)
                    }
                    "Cerrar" -> {
                        findNavController().navigate(R.id.Estadistica_to_Bienvenida)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val datos = USUARIO_ACTIVO.getDatosPromediadosFecha()
            withContext(Dispatchers.Main){
                agregar_datos_grafico(graficoHumedad, datos, "Humedad")
                agregar_datos_grafico(graficoLuminosidad, datos, "Luminosidad")
                agregar_datos_grafico(graficaTemperatura, datos, "Temperatura")
            }
        }

        val ejexH = graficoHumedad.xAxis
        ejexH.labelRotationAngle = -45f
        ejexH.valueFormatter = object : ValueFormatter() {
            private val mFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

            override fun getFormattedValue(value: Float): String {
                return mFormat.format(Date(value.toLong())) // Convertir el valor a Long aquí
            }
        }

        val ejexL = graficoLuminosidad.xAxis
        ejexL.labelRotationAngle = -45f
        ejexL.valueFormatter = object : ValueFormatter() {
            private val mFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

            override fun getFormattedValue(value: Float): String {
                return mFormat.format(Date(value.toLong())) // Convertir el valor a Long aquí
            }
        }

        val ejexT = graficaTemperatura.xAxis
        ejexT.labelRotationAngle = -45f
        ejexT.valueFormatter = object : ValueFormatter() {
            private val mFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

            override fun getFormattedValue(value: Float): String {
                return mFormat.format(Date(value.toLong())) // Convertir el valor a Long aquí
            }
        }
        return view
    }

    fun agregar_datos_grafico(grafico: LineChart, datos: List<JsonObject>, nombre: String){
        val entries = ArrayList<Entry>()
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        for (dato in datos){
            val fecha = formatoFecha.parse(dato["fecha"].toString().replace("\"", ""))?.time
            val valor= dato[nombre.toLowerCase()].toString().replace("\"", "").toFloat()
            if (fecha != null) {
                entries.add(Entry(fecha.toFloat(), valor)) // Convertir la fecha a Float aquí
            }
        }
        val dataSet = LineDataSet(entries, nombre)
        val lineData = LineData(dataSet)
        grafico.data = lineData

        grafico.description.isEnabled = false

        grafico.invalidate()
    }
}