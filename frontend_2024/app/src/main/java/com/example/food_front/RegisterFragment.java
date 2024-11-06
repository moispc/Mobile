package com.example.food_front;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterFragment extends Fragment {

    private EditText etNombre, etApellido, etCorreo, etTelefono, etPassword, etPassword2;
    private Button btnRegister;
    private TextView tvError;

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Inicializar las vistas
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etCorreo = view.findViewById(R.id.etCorreo);
        etTelefono = view.findViewById(R.id.etTelefono);
        etPassword = view.findViewById(R.id.etPassword);
        etPassword2 = view.findViewById(R.id.etPassword2);
        btnRegister = view.findViewById(R.id.btnRegister);
        tvError = view.findViewById(R.id.tvError);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    registerUser();
                }
            }
        });

        return view;
    }

    private boolean validateInputs() {
        tvError.setVisibility(View.GONE);

        if (etNombre.getText().toString().trim().length() < 3 || etApellido.getText().toString().trim().length() < 3) {
            tvError.setText("El nombre y apellido deben tener al menos 3 caracteres.");
            tvError.setVisibility(View.VISIBLE);
            return false;
        }

        if (TextUtils.isEmpty(etCorreo.getText()) || !Patterns.EMAIL_ADDRESS.matcher(etCorreo.getText()).matches()) {
            tvError.setText("Por favor ingrese un correo electrónico válido.");
            tvError.setVisibility(View.VISIBLE);
            return false;
        }

        if (etTelefono.getText().toString().trim().length() < 9) {
            tvError.setText("El número de teléfono debe tener al menos 9 dígitos.");
            tvError.setVisibility(View.VISIBLE);
            return false;
        }

        String password = etPassword.getText().toString();
        if (password.length() < 4 || !password.matches(".*[A-Z].*") || !password.matches(".*[0-9].*")) {
            tvError.setText("La contraseña debe tener al menos 4 caracteres, una mayúscula y un número.");
            tvError.setVisibility(View.VISIBLE);
            return false;
        }

        if (!password.equals(etPassword2.getText().toString())) {
            tvError.setText("Las contraseñas no coinciden.");
            tvError.setVisibility(View.VISIBLE);
            return false;
        }

        // Validar contraseña
        if (!isValidPassword(etPassword.getText().toString())) {
            Toast.makeText(getActivity(), "La contraseña debe tener al menos 4 caracteres, una mayúscula y un número", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 4 && password.matches(".*[A-Z].*") && password.matches(".*[0-9].*");
    }

    private void registerUser() {
        String url = "https://backmobile1.onrender.com/appUSERS/register/";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", etCorreo.getText().toString());
            requestBody.put("password", etPassword.getText().toString());
            requestBody.put("nombre", etNombre.getText().toString());
            requestBody.put("apellido", etApellido.getText().toString());
            requestBody.put("telefono", etTelefono.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error al crear el cuerpo de la solicitud", Toast.LENGTH_SHORT).show();
            return;
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                        replaceFragment(new LoginFragment());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error.networkResponse != null && error.networkResponse.statusCode == 409) {
                            tvError.setText("El correo electrónico ya está en uso.");
                            tvError.setVisibility(View.VISIBLE);
                        } else {

                            tvError.setText("El mail se encuentra en uso.");
                            tvError.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
