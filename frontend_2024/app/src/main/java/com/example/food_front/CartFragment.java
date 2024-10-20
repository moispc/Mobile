package com.example.food_front;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_front.adapters.CarritoAdapter;
import com.example.food_front.models.Carrito;
import com.example.food_front.models.Producto;

public class CartFragment extends Fragment {

    private RecyclerView recyclerViewCarrito;
    private CarritoAdapter carritoAdapter;
    private Carrito carrito;  // Instancia del carrito
    private TextView totalPrecio;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerViewCarrito = view.findViewById(R.id.recyclerview_carrito);
        recyclerViewCarrito.setLayoutManager(new LinearLayoutManager(getContext()));

        totalPrecio = view.findViewById(R.id.text_total_precio);

        carrito = new Carrito();  // Instancia del carrito
        carritoAdapter = new CarritoAdapter(carrito.obtenerProductos(), new CarritoAdapter.OnProductoClickListener() {
            @Override
            public void onEliminarProductoClick(Producto producto) {
                carrito.eliminarProducto(producto);
                carritoAdapter.notifyDataSetChanged();
                actualizarTotal();  // Actualiza el total cuando se elimina un producto
                Toast.makeText(getContext(), "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerViewCarrito.setAdapter(carritoAdapter);
        actualizarTotal();  // Actualizar el total cuando se cargan los productos

        return view;
    }

    private void actualizarTotal() {
        double total = 0;
        for (Producto producto : carrito.obtenerProductos()) {
            total += producto.getPrecio();
        }
        totalPrecio.setText("Total: $" + total);
    }
}
