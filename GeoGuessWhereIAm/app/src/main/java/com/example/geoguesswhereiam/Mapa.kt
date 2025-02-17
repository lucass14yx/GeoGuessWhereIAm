package com.example.geoguesswhereiam

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.geoguesswhereiam.databinding.ActivityMapaBinding
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.MinimapOverlay
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Mapa : AppCompatActivity(), MapEventsReceiver {
    private val MULTIPLE_PERMISSION_REQUEST_CODE: Int = 4
    private lateinit var mapView: MapView
    private lateinit var binding: ActivityMapaBinding
    private var radio: Int = 0
    private var puntuacion: Int = 0
    private var intentos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Manejar permisos primero, antes de que se cree el mapa.
        checkPermissionsState()

        // ¡Importante! Establece tu agente de usuario para evitar ser baneado de los servidores de OSM.
        Configuration.getInstance().setUserAgentValue(packageName)

        binding = ActivityMapaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = binding.map

        val dificultad = SeleccionUsuario.dificultad

        intentos = when (dificultad) {
            1 -> 15
            2 -> 10
            3 -> 5
            else -> 0
        }
        binding.txtPuntuacion.text = intentos.toString()

        val imagen = SeleccionUsuario.imagen
        if (imagen == null) {
            Toast.makeText(this, "No image selected", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Toast.makeText(this, "Lugar seleccionado: ${imagen.nombre}", Toast.LENGTH_LONG).show()
        radio = SeleccionUsuario.radio
        setupMap()
        createMarkers()
        myLocation()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    // Verificar el estado de los permisos y solicitarlos si no están concedidos.
    private fun checkPermissionsState() {
        val fineLocationPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (fineLocationPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MULTIPLE_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Manejar el resultado de la solicitud de permisos.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MULTIPLE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.any { it == PackageManager.PERMISSION_DENIED }) {
                Toast.makeText(
                    this,
                    "No se pueden cargar los mapas sin todos los permisos concedidos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Inicializar el mapa y sus superposiciones.
    private fun setupMap() {
        mapView.isClickable = true
        mapView.setDestroyMode(false)
        mapView.setTileSource(TileSourceFactory.MAPNIK) // Define la fuente de mosaicos
        mapView.setMultiTouchControls(true) // Habilita los controles multitáctiles
        mapView.getLocalVisibleRect(Rect())

        // Añadir un listener para eventos del mapa para añadir marcadores y círculos al hacer clic.
        mapView.overlays.add(MapEventsOverlay(this))

        // Añadir una superposición de brújula al mapa.
        val compassOverlay = CompassOverlay(this, InternalCompassOrientationProvider(this), mapView)
        compassOverlay.enableCompass()
        mapView.overlays.add(compassOverlay)

        // Añadir una superposición de barra de escala al mapa.
        val dm: DisplayMetrics = this.resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(mapView)
        scaleBarOverlay.setCentred(true)
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 40)
        mapView.overlays.add(scaleBarOverlay)

        // Añadir una superposición de minimapa al mapa.
        val minimapOverlay = MinimapOverlay(this, mapView.tileRequestCompleteHandler)
        minimapOverlay.setWidth(dm.widthPixels / 5)
        minimapOverlay.setHeight(dm.heightPixels / 5)
        minimapOverlay.setTileSource(TileSourceFactory.OpenTopo)
        mapView.overlays.add(minimapOverlay)
    }

    // Habilitar y configurar la superposición de ubicación del usuario en el mapa.
    private fun myLocation() {
        val mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mapView)
        mLocationOverlay.enableMyLocation()
        mLocationOverlay.enableFollowLocation() // Seguir la ubicación del usuario
        val icon: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.you)
        mLocationOverlay.setDirectionIcon(icon)
        mLocationOverlay.runOnFirstFix {
            runOnUiThread {
                // Centrar y hacer zoom a la ubicación del usuario cuando esté disponible.
                mapView.controller.setCenter(mLocationOverlay.myLocation)
                mapView.controller.animateTo(mLocationOverlay.myLocation)
                mapView.controller.setZoom(7.3)
                mapView.invalidate() // Redibujar el mapa
            }
        }
        mapView.overlays.add(mLocationOverlay)
    }

    // Crear marcadores en el mapa (implementación necesaria si se requiere).
    fun createMarkers() {
        // Implementar la lógica de creación de marcadores si es necesario
    }

    // Manejar eventos de toque único en el mapa.
    override fun singleTapConfirmedHelper(point: GeoPoint?): Boolean {
        // Añadir un marcador en la ubicación tocada.
        val marker = Marker(mapView)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapView.overlays.add(marker)

        // Añadir un círculo con un radio dependiendo de la dificultad.
        val circle = Polygon(mapView)
        circle.points = Polygon.pointsAsCircle(point, radio.toDouble())
        circle.fillPaint.color = Color.argb(50, 0, 0, 255) // Azul semitransparente
        circle.fillPaint.strokeWidth = 2.0f
        mapView.overlays.add(circle)

        val imagen = SeleccionUsuario.imagen
        if (imagen == null) {
            Toast.makeText(this, R.string.selecciona_imagen, Toast.LENGTH_SHORT).show()
            return true
        }

        val geoPoint = GeoPoint(imagen.altitud, imagen.latitud)
        val acierto = estaDentroDelRadio(point, geoPoint, radio.toDouble())
        val intent = Intent(this, Inicio::class.java)
        if (acierto) {
            val mediaPlayer = MediaPlayer.create(this, R.raw.acierto)

            if(SeleccionUsuario.sound) {
                mediaPlayer.start()
            }

            // Mostrar diálogo de éxito.
            if (!isFinishing && !isDestroyed) {
                showDialog(this)
                SeleccionUsuario.puntosTotales += 1
                imagen.acertada = true
            }
            Handler().postDelayed({
                if (!isFinishing && !isDestroyed) {
                    // Volver a la vista anterior.
                    startActivity(intent)
                    finish()
                }
            }, 3000)
        } else {
            intentos -= 1
            binding.txtPuntuacion.text = intentos.toString()

            // Calcular el rumbo y determinar la dirección.
            val bearing = calculateBearing(point!!, geoPoint)
            val direction = when {
                bearing in 45.0..135.0 -> R.string.este
                bearing in 135.0..225.0 -> R.string.sur
                bearing in 225.0..315.0 -> R.string.oeste
                else -> R.string.norte
            }

            Toast.makeText(this, getString(R.string.fallo_direccion) + " " + getString(direction), Toast.LENGTH_SHORT).show()

            if (intentos == 0) {
                // Mostrar diálogo de fallo.
                if (!isFinishing && !isDestroyed) {
                    showDialogFallo(this)
                }
                Handler().postDelayed({
                    if (!isFinishing && !isDestroyed) {
                        // Volver a la vista anterior.
                        startActivity(intent)
                        finish()
                    }
                }, 3000)
            }
        }
        mapView.invalidate() // Redibujar el mapa
        return true
    }

    // Manejar eventos de pulsación larga en el mapa (implementación necesaria si se requiere).
    override fun longPressHelper(p: GeoPoint?): Boolean {
        // Manejar pulsación larga si es necesario
        return false
    }

    // Calcular el rumbo entre dos GeoPoints.
    private fun calculateBearing(start: GeoPoint, end: GeoPoint): Double {
        val lat1 = Math.toRadians(start.latitude)
        val lon1 = Math.toRadians(start.longitude)
        val lat2 = Math.toRadians(end.latitude)
        val lon2 = Math.toRadians(end.longitude)

        val dLon = lon2 - lon1
        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)
        return (Math.toDegrees(atan2(y, x)) + 360) % 360
    }

    // Verificar si un punto seleccionado está dentro de un radio dado de un punto objetivo.
    private fun estaDentroDelRadio(puntoSeleccionado: GeoPoint?, puntoObjetivo: GeoPoint, radio: Double): Boolean {
        if (puntoSeleccionado == null) return false
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

// Mostrar un diálogo de éxito.
private fun showDialog(context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(R.string.victoria)
    builder.setMessage(R.string.acierto)
    builder.setPositiveButton("Accept") { dialog, _ ->
        dialog.dismiss()
    }
    val dialog = builder.create()
    dialog.show()
}

// Mostrar un diálogo de fallo.
private fun showDialogFallo(context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(R.string.derrota)
    builder.setMessage(R.string.error)
    builder.setPositiveButton("Accept") { dialog, _ ->
        dialog.dismiss()
    }
    val dialog = builder.create()
    dialog.show()
}