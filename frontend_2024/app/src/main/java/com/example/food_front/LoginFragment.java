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
import com.example.food_front.utils.ProfileManager;
import com.example.food_front.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    private EditText etCorreo, etPassword;
    private SessionManager sessionManager;
    private ProfileManager profileManager;

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
        profileManager = new ProfileManager(requireContext());

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

        // Validar campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar correo electrónico
        if (!isValidEmail(email)) {
            Toast.makeText(getContext(), "Por favor, ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar contraseña
        if (!isValidPassword(password)) {
            Toast.makeText(getContext(), "La contraseña debe tener al menos 4 caracteres, una mayúscula y un número", Toast.LENGTH_SHORT).show();
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
                            String name = response.getString("nombre");
                            String surname = response.getString("apellido");
                            String email = response.getString("email");
                            sessionManager.saveToken(token);  // Save token for future use
                            profileManager.saveInfo(name, surname, email);  // Save info for future use
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
                // Manejo de errores dependiendo del estado del servidor
                String errorMessage = error.networkResponse != null && error.networkResponse.data != null
                        ? new String(error.networkResponse.data)
                        : "Error en el inicio de sesión";

                if (errorMessage.contains("usuario no encontrado") || errorMessage.contains("no se reconoció")) {
                    Toast.makeText(getContext(), "Usuario no se reconoció o no existe. Por favor, regístrate.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Agregar la request a la queue de Volley
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

    private boolean isValidEmail(String email) {
        // Regex para validar el correo electrónico
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private boolean isValidPassword(String password) {
        // Comprobar que la contraseña tenga al menos 4 caracteres, una mayúscula y un número
        return password.length() >= 4 && password.matches(".*[A-Z].*") && password.matches(".*[0-9].*");
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

