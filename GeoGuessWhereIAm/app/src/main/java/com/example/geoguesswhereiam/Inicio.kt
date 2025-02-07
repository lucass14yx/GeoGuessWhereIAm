package com.example.geoguesswhereiam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.geoguesswhereiam.databinding.ActivityInicioBinding
import recyclerview.AdaptadorImagenes
import recyclerview.ImagenesProvider

class Inicio : AppCompatActivity() {
    private lateinit var binding: ActivityInicioBinding
    private lateinit var seleccionUsuario: SeleccionUsuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()

        binding.btnFacil.setOnClickListener {
            seleccionUsuario.dificultad = "facil"
        }
        binding.btnMedia.setOnClickListener {
            seleccionUsuario.dificultad = "medio"
        }
        binding.btnDificil.setOnClickListener {
            seleccionUsuario.dificultad = "dificil"
        }

        binding.btnJugar.setOnClickListener {
            val intent = Intent(this, Mapa::class.java)
            Toast.makeText(this, "Dificultad: ${seleccionUsuario.dificultad}, Lugar: ${seleccionUsuario.lugar}", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
    }

    private fun initRecyclerView() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImages.layoutManager = manager

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvImages)

        binding.rvImages.adapter = AdaptadorImagenes(ImagenesProvider.imagenesList)

        val decoration = DividerItemDecoration(this, manager.orientation)
        binding.rvImages.addItemDecoration(decoration)

        binding.rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val snapView = snapHelper.findSnapView(layoutManager) ?: return
                    val snapPosition = layoutManager.getPosition(snapView)
                    val imagen = ImagenesProvider.imagenesList[snapPosition]
                    seleccionUsuario.lugar = imagen
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val snapView = snapHelper.findSnapView(layoutManager) ?: return
                val snapPosition = layoutManager.getPosition(snapView)
                val imagen = ImagenesProvider.imagenesList[snapPosition]
                seleccionUsuario.lugar = imagen
            }
        })
    }
}