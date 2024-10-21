package com.example.food_front;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ProductsFragment extends Fragment {

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_products, container, false);

        ImageView firstImageView = view.findViewById(R.id.imageView1);

        getProductList();

        firstImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace fragment on button click
                replaceFragment(new SelectedProductFragment());  // Replace with another fragment
            }
        });


        return view;
    }


    private void getProductList() {
        String url = "https://backmobile1.onrender.com/api/producto/";
        Log.d("Lista de productos", "URL: " + url);

        // Crear el request de volley
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Mostrar toda la respuesta por consola
                        Log.d("Product Response", response.toString());

                        // Procesar la respuesta aquí
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject product = response.getJSONObject(i);
                                String productName = product.getString("nombre_producto");
                                String prodctPrice = product.getString("precio");
                                String productDescription = product.getString("descripcion");
                                String productImage = product.getString("imageURL");

                                Log.d("Product Name", productName);
                            }

                            Toast.makeText(getContext(), "Lista cargada", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Invalid response from server", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", "Error: " + error.toString());
                if (error.networkResponse != null) {
                    Log.e("Volley Error", "Status Code: " + error.networkResponse.statusCode);
                }
                Toast.makeText(getContext(), "Error en la carga de datos: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar la request a la queue de Volley
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }


    private void replaceFragment(Fragment newFragment) {
        // Get the FragmentManager and start a transaction

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}