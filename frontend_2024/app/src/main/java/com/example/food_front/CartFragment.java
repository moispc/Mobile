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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.food_front.adapters.CarritoAdapter;
import com.example.food_front.models.Carrito;
import com.example.food_front.models.Producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    private RecyclerView recyclerViewCarrito;
    private CarritoAdapter carritoAdapter;
    private Carrito carrito;  // Instancia del carrito
    private TextView totalPrecio;
    private RequestQueue requestQueue; // Cola de solicitudes de Volley

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerViewCarrito = view.findViewById(R.id.recyclerview_carrito);
        recyclerViewCarrito.setLayoutManager(new LinearLayoutManager(getContext()));


        totalPrecio = view.findViewById(R.id.text_total_precio);

        // Inicializa la cola de solicitudes
        requestQueue = Volley.newRequestQueue(getContext());

        // Inicializa el carrito
        carrito = Carrito.getInstance();
        carritoAdapter = new CarritoAdapter(carrito.obtenerProductos(), new CarritoAdapter.OnProductoClickListener() {
            @Override
            public void onEliminarProductoClick(Producto producto) {
                eliminarProductoDelCarrito(producto);
            }
        });

        recyclerViewCarrito.setAdapter(carritoAdapter);
        actualizarTotal();  // Actualiza el total al cargar los productos

        // Cargar los productos desde la API
        cargarProductosDesdeAPI();

        return view;
    }

    private void cargarProductosDesdeAPI() {
        String url = "https://backmobile1.onrender.com/appCART/ver/"; // Cambia esta URL según tu API



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray productosArray = response.getJSONArray("productos"); // Ajusta según tu respuesta
                            ArrayList<Producto> productos = new ArrayList<>();

                            for (int i = 0; i < productosArray.length(); i++) {
                                JSONObject productoJson = productosArray.getJSONObject(i);
                                Producto producto = new Producto(
                                        Integer.parseInt(productoJson.getString("id")), // Ajusta según tu modelo
                                        productoJson.getString("nombre"),
                                        productoJson.getString("descripcion"),
                                        productoJson.getDouble("precio"),
                                        productoJson.getString("imagenUrl") // Ajusta según tu modelo
                                );
                                productos.add(producto);
                            }

                            carrito.agregarProductos(productos); // Agrega los productos al carrito
                            carritoAdapter.notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
                            actualizarTotal(); // Actualiza el total
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error al cargar productos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), "Error en la solicitud", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest); // Agrega la solicitud a la cola
    }

    private void eliminarProductoDelCarrito(Producto producto) {
        // Lógica para eliminar el producto del carrito
        carrito.eliminarProducto(producto);
        carritoAdapter.notifyDataSetChanged();
        actualizarTotal();  // Actualiza el total cuando se elimina un producto
        Toast.makeText(getContext(), "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
    }

    private void actualizarTotal() {
        double total = 0;
        for (Producto producto : carrito.obtenerProductos()) {
            total += producto.getPrecio();
        }
        // Muestra el total formateado con dos decimales
        totalPrecio.setText(String.format("Total: $%.2f", total));
    }
}
