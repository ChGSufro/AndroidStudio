package com.example.appandroid.main
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appandroid.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_host)
    }
}