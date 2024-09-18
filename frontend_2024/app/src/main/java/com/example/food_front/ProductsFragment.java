package com.example.food_front;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ProductsFragment extends Fragment {

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_products, container, false);

        ImageView firstImageView = view.findViewById(R.id.imageView1);
        ImageView secondImageView = view.findViewById(R.id.imageView2);
        ImageView thirdImageView = view.findViewById(R.id.imageView3);


        firstImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace fragment on button click
                replaceFragment(new SelectedProductFragment());  // Replace with another fragment
            }
        });

        secondImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace fragment on button click
                replaceFragment(new SelectedProductFragment());  // Replace with another fragment
            }
        });

        thirdImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace fragment on button click
                replaceFragment(new SelectedProductFragment());  // Replace with another fragment
            }
        });







        return view;
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