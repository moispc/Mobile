package com.example.food_front;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.food_front.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    private EditText etCorreo, etPassword;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Inicializar las vistas
        etCorreo = view.findViewById(R.id.etCorreo);
        etPassword = view.findViewById(R.id.etPassword);
        Button btnLogin = view.findViewById(R.id.btnLogin);
        TextView tvRegister = view.findViewById(R.id.tvRegister);

        sessionManager = new SessionManager(requireContext());

        // Agregar el click listener al boton de login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        // Agregar el click listener al texto de registro
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new RegisterFragment());
            }
        });

        return view;
    }

    private void performLogin() {
        String email = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Controlar que no haya imputs vacios
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar formato de correo electrónico
        if (!isValidEmail(email)) {
            Toast.makeText(getContext(), "El correo electrónico no tiene un formato válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar la contraseña
        if (!isValidPassword(password)) {
            Toast.makeText(getContext(), "La contraseña debe tener al menos 4 caracteres y al menos un número", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://backmobile1.onrender.com/appUSERS/login/";

        // Crear el json que se enviará en el body
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", email);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Crear el request de volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String token = response.getString("access");
                            sessionManager.saveToken(token);  // Save token for future use
                            Toast.makeText(getContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            replaceFragment(new HomeFragment());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Respuesta inválida del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error en el inicio de sesión", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar la request a la queue de Volley
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 4 && password.matches(".*\\d.*");
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
