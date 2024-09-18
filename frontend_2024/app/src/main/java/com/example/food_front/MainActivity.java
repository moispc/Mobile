package com.example.food_front;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.food_front.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    mostrarHome(); // Show HomeFragment
                    return true;
                } else if (itemId == R.id.profile) {
                    mostrarPerfil(); // Show ProfileFragment
                    return true;
                } else if (itemId == R.id.carrito) {
//                    mostrarCarrito(); // Show CartFragment
                    return true;
                } else if (itemId == R.id.contact) {
//                    mostrarContact(); // Show ContactFragment
                    return true;
                }

                return false;
            }
        });

    }

    public void mostrarHome() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container_view, new HomeFragment());
        fragmentTransaction.commit();
    }

    // You can add methods to show other fragments like SearchFragment or ProfileFragment
    public void mostrarPerfil() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container_view, new LoginFragment());
        fragmentTransaction.commit();
    }

}