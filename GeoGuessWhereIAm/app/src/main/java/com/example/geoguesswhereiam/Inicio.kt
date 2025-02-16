package com.example.geoguesswhereiam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.geoguesswhereiam.databinding.ActivityInicioBinding
import org.osmdroid.util.GeoPoint
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import recyclerview.AdaptadorImagenes
import recyclerview.ImagenesProvider

class Inicio : AppCompatActivity() {
    private lateinit var binding: ActivityInicioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Configurar Toolbar
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
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
            if(SeleccionUsuario.dificultad == 0) {
                Toast.makeText(this, "Selecciona una dificultad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(SeleccionUsuario.imagen.acertada) {
                Toast.makeText(this, "Ya has acertado este lugar!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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

    private fun asignarImagenVisible(binding: ActivityInicioBinding) {
        val layoutManager = binding.rvImages.layoutManager as LinearLayoutManager
        val position = layoutManager.findFirstVisibleItemPosition()
        SeleccionUsuario.imagen = ImagenesProvider.imagenesList[position]
    }

    private fun estaDentroDelRadio(puntoSeleccionado: GeoPoint, puntoObjetivo: GeoPoint, radio: Double): Boolean {
        val radioTierra = 6371000.0 // Radio de la Tierra en metros

        val lat1 = Math.toRadians(puntoSeleccionado.latitude)
        val lon1 = Math.toRadians(puntoSeleccionado.longitude)
        val lat2 = Math.toRadians(puntoObjetivo.latitude)
        val lon2 = Math.toRadians(puntoObjetivo.longitude)

        val distancia = radioTierra * acos(
            sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(lon2 - lon1)
        )

        return distancia <= radio
    }
}