package com.example.geoguesswhereiam

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.geoguesswhereiam.databinding.ActivityContenedorPuntuacionBinding
import recyclerview.ImagenesProvider

class ContenedorPuntuacion : AppCompatActivity() {
    private lateinit var binding : ActivityContenedorPuntuacionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContenedorPuntuacionBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Asigna los puntos totales al TextView
        binding.txtPNum.text = SeleccionUsuario.puntosTotales.toString()

        // Se recorre la lista de imagenes y se muestra el nombre de las imagenes acertadas
        ImagenesProvider.imagenesList.forEach {
            if(it.acertada){
                binding.txtLista.text = binding.txtLista.text.toString() + it.nombre
            }
        }

    }


}