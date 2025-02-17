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

        binding.txtPNum.text = SeleccionUsuario.puntosTotales.toString()


        ImagenesProvider.imagenesList.forEach {
            if(it.acertada){
                binding.txtLista.text = binding.txtLista.text.toString() + it.nombre
            }
        }



    }


}