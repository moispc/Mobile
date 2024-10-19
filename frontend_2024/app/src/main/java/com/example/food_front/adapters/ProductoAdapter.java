package com.example.food_front.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.food_front.R;
import com.example.food_front.models.Producto;
import com.bumptech.glide.Glide;


import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    //lista de productos
    private List<Producto> listaProductos;

    // Constructor que recibe la lista de productos
    public ProductoAdapter(List<Producto> listaProductos){
        this.listaProductos = listaProductos;
    }

    @NonNull
    @Override
    // Infla el layout para cada ítem de la lista (crea la vista)
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.textViewNombre.setText(producto.getNombre());
        holder.textViewDescripcion.setText(producto.getDescripcion());
        holder.textViewPrecio.setText("$" + producto.getPrecio());

        // Cargar la imagen
        String imageUrl = producto.getImagenUrl();
        if (imageUrl != null) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder) // Imagen por defecto mientras se carga
                    .error(R.drawable.error_image) // Imagen de error si no se carga
                    .into(holder.imageViewProducto); // Usar imageViewProducto aquí
        } else {
            holder.imageViewProducto.setImageResource(R.drawable.placeholder); // Imagen predeterminada si no hay URL
        }
    }
    // Vincula los datos con las vistas
    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProducto;
        TextView textViewNombre;
        TextView textViewDescripcion;
        TextView textViewPrecio;

        // ViewHolder que almacena las referencias a los elementos del layout de cada ítem
        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProducto = itemView.findViewById(R.id.imageViewProducto);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion);
            textViewPrecio = itemView.findViewById(R.id.textViewPrecio);
        }
    }
}
