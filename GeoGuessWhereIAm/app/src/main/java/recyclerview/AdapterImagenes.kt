package recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geoguesswhereiam.R


class AdaptadorImagenes(private val imageNames: List<String>) : RecyclerView.Adapter<ImagenesViewHolder>() {
    private var data: List<String>
    init {
        data = imageNames
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagenesViewHolder {
        // Inflamos el layout de cada elemento
        val layoutInflater = LayoutInflater.from(parent.context)
        return ImagenesViewHolder(layoutInflater.inflate(R.layout.item_imagenes, parent, false))
    }
    override fun onBindViewHolder(holder: ImagenesViewHolder, position: Int) {
        // Inicializamos la lista de imagenes
        val imagenName = data[position]
        //accedo al imageView, por el nombre, no puedo usar R.drawable.imagename
        val imageResourceId = holder.itemView.context.resources.getIdentifier( // Use holder.itemView.context
            imagenName,"drawable",holder.itemView.context.packageName)
        holder.imagenView.setImageResource(imageResourceId)
    }
    override fun getItemCount(): Int {
        return data.size
    }

}