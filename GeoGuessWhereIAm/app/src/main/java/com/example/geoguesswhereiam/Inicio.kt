package com.example.geoguesswhereiam

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

        binding.btnSwitchVolume.setOnClickListener {
            if (SeleccionUsuario.sound) {
                binding.btnSwitchVolume.setImageResource(R.drawable.volumenoff)
                SeleccionUsuario.sound = false
                Toast.makeText(this, R.string.sonidooff, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.sonidoon, Toast.LENGTH_SHORT).show()
                binding.btnSwitchVolume.setImageResource(R.drawable.volumenon)
                SeleccionUsuario.sound = true
            }
        }

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
            if (SeleccionUsuario.dificultad == 0) {
                Toast.makeText(this, R.string.selecciona_dificultad, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (SeleccionUsuario.imagen == null) {
                Toast.makeText(this, R.string.selecciona_imagen, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (SeleccionUsuario.imagen!!.acertada) {
                Toast.makeText(this, R.string.acertado, Toast.LENGTH_SHORT).show()
                if(SeleccionUsuario.imagen!!.nombre == "Toledo") {
                    val intent = Intent(this, VideoView::class.java)
                    startActivity(intent)
                }
                return@setOnClickListener
            }

            val intent = Intent(this, Mapa::class.java)
            asignarImagenVisible(binding)
            startActivity(intent)
            finish()
        }

        binding.rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvImages)
        binding.rvImages.adapter = AdaptadorImagenes(ImagenesProvider.imagenesList)
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

    // Método onCreateOptionsMenu para inflar el menú
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    // Método onOptionsItemSelected para manejar los clics en los elementos del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemPtotal -> {
                val intent = Intent(this, ContenedorPuntuacion::class.java)
                startActivity(intent)
                true
            }
            R.id.itemAcercaDe -> {
                val intent = Intent(this, ContenedorAcercaDe::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}