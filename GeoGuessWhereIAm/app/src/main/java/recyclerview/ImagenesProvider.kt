package recyclerview

import com.example.geoguesswhereiam.Imagen
import com.example.geoguesswhereiam.R

object ImagenesProvider {

    // Proveedor de imágenes que se mostrarán en el RecyclerView

    val imagenesList = arrayListOf(
        Imagen(R.drawable.alicante, 38.34517, -0.48149,"Alicante",false),
        Imagen(R.drawable.navarra, 42.69539, -1.67607,"Navarra",false),
        Imagen(R.drawable.pontevedra, 42.43142, -8.64426,"Pontevedra",false),
        Imagen(R.drawable.sevilla, 37.38283, -5.97317,"Sevilla",false),
        Imagen(R.drawable.toledoo, 39.8581, -4.02263,"Toledo",false)
    )

}