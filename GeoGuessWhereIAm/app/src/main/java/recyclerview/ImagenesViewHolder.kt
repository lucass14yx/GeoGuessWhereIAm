package recyclerview

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.geoguesswhereiam.databinding.ItemImagenesBinding

class ImagenesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // Binding de la vista de la imagen en el RecyclerView
    private val binding = ItemImagenesBinding.bind(itemView)
    // Establece la imagen en el ImageView
    val imagenView: ImageView = binding.idImagenView
}