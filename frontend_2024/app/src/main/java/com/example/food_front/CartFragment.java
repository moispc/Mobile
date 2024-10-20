package com.example.food_front;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.food_front.adapters.CarritoAdapter;
import com.example.food_front.models.Carrito;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private CarritoAdapter adapter;
    private List<Carrito> listaCarrito;


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        //Inicializar el recyclerView
        recyclerView = view.findViewById(R.id.recyclerview_carrito);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // listado del carrito
        listaCarrito = new ArrayList<>();

        // asegurar el contexto
        adapter = new CarritoAdapter(listaCarrito);
        recyclerView.setAdapter(adapter);

        //Cargar productos del carrito
        loadCarrito();

        Button button = view.findViewById(R.id.btnPagar);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new DatosEntregaFragment());  // Replace with another fragment
            }
        });

        return view;
    }
    private void loadCarrito() {
        String url = "https://backmobile1.onrender.com/appCART/ver/";

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
                                JSONObject cartJson = jsonArray.getJSONObject(i);
                                String product = cartJson.getString("nombre_producto");
                                int quantity = cartJson.getInt("cantidad");
                                double price = cartJson.getDouble("precio");
                                String imageUrl = cartJson.optString("imageURL", "");

                                Carrito carrito = new Carrito(product, quantity, price, imageUrl);
                                carritoList.add(carrito);
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
    private void replaceFragment(Fragment newFragment) {
        // Get the FragmentManager and start a transaction

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}