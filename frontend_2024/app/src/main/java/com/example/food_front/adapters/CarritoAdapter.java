package com.example.food_front.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_front.R;
import com.example.food_front.models.Producto;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder> {

    private List<Producto> productos;
    private OnProductoClickListener listener;

    public CarritoAdapter(List<Producto> productos, OnProductoClickListener listener) {
        this.productos = productos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_cart, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.nombreProducto.setText(producto.getNombre());
        holder.precioProducto.setText(String.valueOf(producto.getPrecio()));

//        // Cargar la imagen usando Glide
//        Glide.with(holder.itemView.getContext())
//                .load(producto.getImagenUrl())
//                .into(holder.imageproducto);

        holder.eliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEliminarProductoClick(producto);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class CarritoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreProducto, precioProducto;
        ImageView eliminarProducto;

        public CarritoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreProducto = itemView.findViewById(R.id.text_nombre_producto);
            precioProducto = itemView.findViewById(R.id.text_precio_producto);
            eliminarProducto = itemView.findViewById(R.id.image_eliminar_producto);
        }
    }

    public interface OnProductoClickListener {
        void onEliminarProductoClick(Producto producto);
    }
}
