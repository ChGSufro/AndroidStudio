package com.example.appandroid.main

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.appandroid.R


class MainActivity : AppCompatActivity() {

    private val BLUETOOTH_SCAN_PERMISSION_REQUEST_CODE = 3

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_host)

        val bluetoothManager = getSystemService(BluetoothManager::class.java)// Se obtiene el servicio BluetoothManager, que es el encargado de administrar el Bluetooth
        val bluetoothAdapter = bluetoothManager.adapter// Se obtiene el adaptador Bluetooth de nuestro dispositivo

        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT)

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, BLUETOOTH_SCAN_PERMISSION_REQUEST_CODE)
            }
        }

        if (!bluetoothAdapter.isEnabled){
            Toast.makeText(this, "Su bluetooth esta apagado.", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            BLUETOOTH_SCAN_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "La app necesita permisos especiales.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                return
            }
        }
    }
}