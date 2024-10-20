package com.example.food_front.adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.food_front.R;
import com.example.food_front.models.Carrito;

import java.util.List;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>{
    // listado del carrito
    private List<Carrito> listaCarrito;

    //constructor que recibe el listado del carrito
    public CarritoAdapter(List<Carrito> listaCarrito){
        this.listaCarrito = listaCarrito;
    }

    @NonNull
    @Override

    // infla el layout para cada item del carrito (creando la vista)

    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new CarritoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CarritoAdapter.CarritoViewHolder holder, int position) {
        Carrito carrito = listaCarrito.get(position);
        holder.textViewNombre.setText(carrito.getProducto());
        holder.textViewCantidad.setText(carrito.getCantidad());
        holder.textViewPrecio.setText(carrito.getPrecio());

    }

    @Override
    public int getItemCount() {
        return listaCarrito.size();
    }

    static class CarritoViewHolder extends RecyclerView.ViewHolder{
        TextView textViewNombre;
        TextView textViewCantidad;
        TextView textViewPrecio;


        public  CarritoViewHolder(@NonNull View itemView){
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewCantidad = itemView.findViewById(R.id.textViewCantidad);
            textViewPrecio = itemView.findViewById(R.id.textViewPrecio);
        }
    }
}
