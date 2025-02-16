package com.example.geoguesswhereiam

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.geoguesswhereiam.databinding.ActivityContenedorPuntuacionBinding

class ContenedorPuntuacion : AppCompatActivity() {
    private lateinit var binding : ActivityContenedorPuntuacionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContenedorPuntuacionBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.txtPuntuacionTotal.text = SeleccionUsuario.puntosTotales.toString()

    }
}