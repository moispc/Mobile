package com.example.food_front;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.food_front.utils.ProfileManager;

public class ProfileFragment extends Fragment {

    private TextView tvNombre, tvEmail;
    private ProfileManager profileManager;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inicializar las vistas
        tvNombre = view.findViewById(R.id.user_name);
        tvEmail = view.findViewById(R.id.user_email);
        profileManager = new ProfileManager(requireContext());

        // Llamar al backend para obtener los datos del perfil
        displayUserProfile();

        // Encontrar el TextView de "Datos personales"
        TextView personalData = view.findViewById(R.id.personal_data);

        // Agregar un onClickListener para navegar a "Editar perfil"
        personalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la transacción para mostrar el fragmento de "Editar perfil"
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_view, new EditProfileFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void displayUserProfile() {
        // Usar los métodos específicos para obtener los datos
        String name = profileManager.getName();
        String surname = profileManager.getSurname();
        String email = profileManager.getEmail();

        // Mostrar los datos en los TextViews
        tvNombre.setText(name + " " + surname);  // Mostrar nombre completo
        tvEmail.setText(email);
    }

}