package com.example.geoguesswhereiam

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.geoguesswhereiam.databinding.ActivityInicioBinding
import recyclerview.AdaptadorImagenes
import recyclerview.ImagenesProvider


class Inicio : AppCompatActivity() {
    private lateinit var binding: ActivityInicioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()

        binding.btnFacil.setOnClickListener {
            SeleccionUsuario.dificultad = 1 // "facil"
            SeleccionUsuario.radio = 10000
        }
        binding.btnMedia.setOnClickListener {
            SeleccionUsuario.dificultad = 2 // "medio"
            SeleccionUsuario.radio = 5000
        }
        binding.btnDificil.setOnClickListener {
            SeleccionUsuario.dificultad = 3 //"dificil"
            SeleccionUsuario.radio = 2500
        }

        binding.btnJugar.setOnClickListener {
            val intent = Intent(this, Mapa::class.java)
            asignarImagenVisible(binding)
            startActivity(intent)
            finish()
        }

        binding.rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            // Asignar la Imagen de la lista a la variable lugar
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val position = layoutManager.findFirstVisibleItemPosition()
                SeleccionUsuario.imagen = ImagenesProvider.imagenesList[position]
            }

        })

    }

    private fun initRecyclerView() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImages.layoutManager = manager
        // Para el efecto de "snap"
        // efecto de carrusel donde los elementos se centran automáticamente al visualizarlos
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvImages)

        binding.rvImages.adapter = AdaptadorImagenes(ImagenesProvider.imagenesList)

        // Crear un DividerItemDecoration y agregarlo al RecyclerView
        val decoration = DividerItemDecoration(this, manager.orientation)
        binding.rvImages.addItemDecoration(decoration)


    }

    private fun asignarImagenVisible(binding:ActivityInicioBinding){

        val layoutManager = binding.rvImages.layoutManager as LinearLayoutManager
        val position = layoutManager.findFirstVisibleItemPosition()
        SeleccionUsuario.imagen = ImagenesProvider.imagenesList[position]

    }

}