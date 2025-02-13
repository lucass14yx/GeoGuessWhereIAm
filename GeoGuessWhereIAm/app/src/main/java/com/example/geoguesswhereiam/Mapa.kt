package com.example.geoguesswhereiam

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
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
import com.example.geoguesswhereiam.SeleccionUsuario


class Mapa : AppCompatActivity(), MapEventsReceiver {
    private val MULTIPLE_PERMISSION_REQUEST_CODE: Int = 4
    private lateinit var mapView: MapView
    private lateinit var binding: ActivityMapaBinding
    private var radio : Int = 0
    private var puntuacion : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //handle permissions first, before map is created.
        checkPermissionsState()

        //important! set your user agent to prevent getting banned from the osm servers
        Configuration.getInstance().setUserAgentValue(getPackageName())

        binding = ActivityMapaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = binding.map

        var dificultad = SeleccionUsuario.dificultad
        
        if(dificultad == 1)
            intentos = 15
            binding.txtPuntuacion.text = intentos.toString()

        else if(dificultad == 2)
            intentos = 10
            binding.txtPuntuacion.text = intentos.toString()

        else if(dificultad == 3)
            intentos = 5
            binding.txtPuntuacion.text = intentos.toString()

        Toast.makeText(this, "Lugar seleccionado: ${SeleccionUsuario.imagen.nombre}", Toast.LENGTH_LONG).show()
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

    private fun checkPermissionsState() {
        val fineLocationPermissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (fineLocationPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ),
                MULTIPLE_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MULTIPLE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    var somePermissionWasDenied = false
                    for (result in grantResults) {
                        if (result == PackageManager.PERMISSION_DENIED) {
                            somePermissionWasDenied = true
                        }
                    }
                    if (somePermissionWasDenied) {
                        Toast.makeText(
                            this,
                            "Cant load maps without all the permissions granted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Cant load maps without all the permissions granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }

        }
    }

    private fun setupMap(){
        //Inicializar map
        mapView.setClickable(true);
        mapView.setDestroyMode(false);
        mapView.setTileSource(TileSourceFactory.MAPNIK) // Define la fuente de mosaicos
        mapView.setMultiTouchControls(true)// Habilita los controles multitáctiles
        mapView.getLocalVisibleRect(Rect())

        // MainActivity al implementar MapEventsReceiver, es un listener
        // que al hacer clic se añade un marcador al mapa
        // y un circulo
        mapView.overlays.add(MapEventsOverlay(this))

        //superponer brújula
        var compassOverlay = CompassOverlay(this, InternalCompassOrientationProvider(this), mapView)
        compassOverlay.enableCompass()
        mapView.overlays.add(compassOverlay)

        //agregar barra de escala en el mapa
        val dm : DisplayMetrics = this.resources.displayMetrics
        val scaleBarOverlay = ScaleBarOverlay(mapView)
        scaleBarOverlay.setCentred(true)
        //ubicacion en la app de la barra de escala
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 40)
        mapView.overlays.add(scaleBarOverlay)

        //agregar minimapa
        val minimapOverlay = MinimapOverlay(this, mapView.tileRequestCompleteHandler)
        minimapOverlay.setWidth(dm.widthPixels / 5)
        minimapOverlay.setHeight(dm.heightPixels / 5)
        //ponemos una fuente diferente al minimapa (opcional)
        minimapOverlay.setTileSource(TileSourceFactory.OpenTopo)
        mapView.overlays.add(minimapOverlay)

    }

    private fun myLocation() {

        var mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mapView)
        mLocationOverlay.enableMyLocation()
        //si deseas que el mapa siga la ubicación del usuario
        mLocationOverlay.enableFollowLocation()
        //cambiar icono
        val icon: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.coche)
        mLocationOverlay.setDirectionIcon(icon)
        mLocationOverlay.runOnFirstFix {
            runOnUiThread {
                //cuando tengamos ubicación
                //Centrar y hacer Zoom hacia un marcador
                mapView.controller.setCenter(mLocationOverlay.myLocation);
                mapView.controller.animateTo(mLocationOverlay.myLocation)
                mapView.controller.setZoom(7.3)
                mapView.invalidate() // Redibujar el mapa
            }
        }
        mapView.overlays.add(mLocationOverlay)
    }

    fun createMarkers() {


    }

    override fun singleTapConfirmedHelper(point: GeoPoint?): Boolean {
        //Maneja el evento clic del mapa
        // Agregar marcador
        val marker = Marker(mapView)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapView.overlays.add(marker)

        // Agregar círculo de un radio dependiendo de la dificultad
        val circle = Polygon(mapView)
        circle.points = Polygon.pointsAsCircle(point, radio.toDouble())
        circle.fillPaint.color= Color.argb(50, 0, 0, 255) // Azul semitransparente
        circle.fillPaint.strokeWidth = 2.0f
        mapView.overlays.add(circle)
        var acierto : Boolean = circle.isCloseTo(point,radio.toDouble(),binding.map)
        if (acierto){
            print("ACIERTO")
        } else {
            puntuacion--
        }
        mapView.invalidate() // Redibujar el mapa
        return true
    }

    override fun longPressHelper(p: GeoPoint?): Boolean {
        // Manejar pulsación larga si es necesario
        return false
    }



}

// Implementar
private fun aciertoFallo(){


}