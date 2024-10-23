package com.example.food_front;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class EditProfileFragment extends Fragment {

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Encontrar la flecha de regreso
        ImageView backArrow = view.findViewById(R.id.back_arrow);

        // Agregar un listener para cuando se haga clic en la flecha
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Al hacer clic en la flecha, volver al fragmento anterior
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(); // Regresar al fragmento anterior
            }
        });

        return view;
    }
}