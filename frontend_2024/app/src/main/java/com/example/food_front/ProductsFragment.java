package com.example.food_front;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.food_front.adapters.ProductoAdapter;
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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.recyclerview_producto);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar la lista de productos
        productList = new ArrayList<>();

        // Aquí se asegura que el contexto ya esté disponible
        adapter = new ProductoAdapter(productList);
        recyclerView.setAdapter(adapter);

        // Cargar productos desde el backend
        loadProducts();

        return view;

    }
    private void loadProducts() {
        String url = "https://backmobile1.onrender.com/api/producto/";

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.d("Response", jsonArray.toString());
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject productJson = jsonArray.getJSONObject(i);
                                String name = productJson.getString("nombre_producto");
                                String description = productJson.getString("descripcion");
                                double price = productJson.getDouble("precio");
                                String imageUrl = productJson.optString("imageURL", "");

                                Producto producto = new Producto(name, description, price, imageUrl);
                                productList.add(producto);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSON Error", "Error parsing JSON response: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", "Error: " + error.getMessage()); // Log de error
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }



}

//    private void replaceFragment(Fragment newFragment) {
//        // Get the FragmentManager and start a transaction
//
//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container_view, newFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }
