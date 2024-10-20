package com.example.food_front;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.food_front.adapters.ProductoAdapter;
import com.example.food_front.models.Carrito;
import com.example.food_front.models.Producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private List<Producto> productList;
    private Carrito carrito; // Instancia del carrito

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_producto);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        carrito = new Carrito(); // Inicializamos el carrito

        productList = new ArrayList<>();
        adapter = new ProductoAdapter(productList, new ProductoAdapter.OnProductoClickListener() {
            @Override
            public void onAgregarCarritoClick(Producto producto) {
                // Guardar en el carrito local
                carrito.agregarProducto(producto);
                Toast.makeText(getContext(), "Producto agregado al carrito", Toast.LENGTH_SHORT).show();
                // Aquí podrías llamar a la función para actualizar el ícono del carrito
                // actualizarIconoCarrito();
            }
        });

        recyclerView.setAdapter(adapter);
        cargarProductos();

        return view;
    }

    private void cargarProductos() {
        String url = "https://backmobile1.onrender.com/api/producto/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsear el JSON
                            JSONArray jsonArray = new JSONArray(response);
                            productList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                // Obtener los detalles del producto desde el JSON
                                int id_producto = jsonObject.getInt("id_producto");
                                String nombre_producto = jsonObject.getString("nombre_producto");
                                String descripcion = jsonObject.getString("descripcion");
                                double precio = jsonObject.getDouble("precio");
                                String imagenUrl = jsonObject.getString("imageURL");

                                // Crear un nuevo objeto Producto
                                Producto producto = new Producto(id_producto, nombre_producto, descripcion, precio, imagenUrl);
                                productList.add(producto);  // Añadir a la lista
                            }

                            // Notificar al adaptador que los datos han cambiado
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ProductsFragment", "Error al parsear el JSON: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProductsFragment", "Error en la solicitud: " + error.getMessage());
                Toast.makeText(getContext(), "Error al cargar productos", Toast.LENGTH_SHORT).show();
            }
        });

        // Añadir la solicitud a la cola
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}
