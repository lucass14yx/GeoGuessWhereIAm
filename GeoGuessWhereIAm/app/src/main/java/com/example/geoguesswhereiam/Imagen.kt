package com.example.geoguesswhereiam

// Clase que representa una imagen con su respectiva altitud, latitud, nombre y si fue acertada o no

class Imagen(
    var recImagen: Int,
    var altitud: Double,
    var latitud: Double,
    var nombre: String = "",
    var acertada: Boolean = false
) 