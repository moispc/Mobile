package com.example.food_front;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class CartFragment extends Fragment {

    private int quantity1 = 1; // Cantidad del primer producto
    private int quantity2 = 3; // Cantidad del segundo producto

    private TextView tvSubTitle1, tvSubTitle2;
    private Button btnPagar;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Inicializar las vistas
        tvSubTitle1 = view.findViewById(R.id.tvSubTitle);
        tvSubTitle2 = view.findViewById(R.id.tvSubTitle3);
        btnPagar = view.findViewById(R.id.btnPagar);
        TextView tvFecha = view.findViewById(R.id.tvFecha); // Inicializa el TextView de la fecha
        ImageView btnBack = view.findViewById(R.id.btnBack);


        // Inicializar las cantidades
        updateQuantityDisplay();

        // Listener para el botón de pagar
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new DatosEntregaFragment());
            }
        });

        // Listener para la flecha de retroceso
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();  // Regresar a la pantalla anterior
            }
        });

        // Listener para sumar y restar del primer producto
        ImageView btnAdd1 = view.findViewById(R.id.tvAdd1);
        ImageView btnRemove1 = view.findViewById(R.id.delete);

        btnAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity1++;
                updateQuantityDisplay();
            }
        });

        btnRemove1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity1 > 1) {
                    quantity1--;
                } else {
                    quantity1 = 0;
                }
                updateQuantityDisplay();
            }
        });

        // Listener para sumar y restar del segundo producto
        ImageView btnAdd2 = view.findViewById(R.id.tvAdd2);
        ImageView btnRemove2 = view.findViewById(R.id.delete2);

        btnAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity2++;
                updateQuantityDisplay();
            }
        });

        btnRemove2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity2 > 1) {
                    quantity2--;
                } else {
                    quantity2 = 0;
                }
                updateQuantityDisplay();
            }
        });

        return view;
    }

    private void updateQuantityDisplay() {
        tvSubTitle1.setText(String.valueOf(quantity1));
        tvSubTitle2.setText(String.valueOf(quantity2));

        // Habilitar o deshabilitar el botón de pagar
        if (quantity1 == 0 && quantity2 == 0) {
            btnPagar.setEnabled(false); // Deshabilitar botón
        } else {
            btnPagar.setEnabled(true); // Habilitar botón
        }
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
