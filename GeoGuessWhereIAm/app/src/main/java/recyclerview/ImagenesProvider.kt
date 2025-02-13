package recyclerview

import com.example.geoguesswhereiam.Imagen
import com.example.geoguesswhereiam.R

object ImagenesProvider {
//    val imagenesList = arrayListOf(
//        "alicante",
//        "navarra",
//        "pontevedra",
//        "sevilla",
//        "toledoo"
//    )

    val imagenesList = arrayListOf(
        Imagen(R.drawable.alicante, 38.34517, -0.48149,"Alicante"),
        Imagen(R.drawable.navarra, 42.69539, -1.67607,"Navarra"),
        Imagen(R.drawable.pontevedra, 42.43142, -8.64426,"Pontevedra"),
        Imagen(R.drawable.sevilla, 37.38283, -5.97317,"Sevilla"),
        Imagen(R.drawable.toledoo, 39.8581, -4.02263,"Toledo")
    )

}