package com.example.food_front;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
    private int loginAttempts = 0;  // Contador de intentos fallidos
    private boolean isBlocked = false;  // Indicador de bloqueo
    private TextView tvEmailError, tvPasswordError;  // Para mostrar los errores

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Inicializar las vistas
        etCorreo = view.findViewById(R.id.etCorreo);
        etPassword = view.findViewById(R.id.etPassword);
        Button btnLogin = view.findViewById(R.id.btnLogin);
        TextView tvRegister = view.findViewById(R.id.tvRegister);
        tvEmailError = view.findViewById(R.id.tvEmailError);
        tvPasswordError = view.findViewById(R.id.tvPasswordError);

        sessionManager = new SessionManager(requireContext());
        profileManager = new ProfileManager(requireContext());

        // Agregar el click listener al boton de login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBlocked) {
                    Toast.makeText(getContext(), "Has excedido el número de intentos. Intenta nuevamente en 10 segundos.", Toast.LENGTH_LONG).show();
                    return;  // Bloqueo temporal
                }
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

        // Limpiar mensajes de error anteriores
        tvEmailError.setVisibility(View.GONE);
        tvPasswordError.setVisibility(View.GONE);

        // Validar campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar correo electrónico
        if (!isValidEmail(email)) {
            tvEmailError.setVisibility(View.VISIBLE);  // Mostrar error de correo
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
                            String phone = response.getString("telefono");

                            sessionManager.saveToken(token);  // Guardar el token para futuras solicitudes
                            profileManager.saveInfo(name, surname, email, phone);  // Save info for future use
                            saveUserProfile(name, surname, email, phone); // Llamada a la nueva función
                            Toast.makeText(getContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                            loginAttempts = 0;  // Resetear el contador de intentos al hacer login exitoso
                            replaceFragment(new HomeFragment());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Respuesta inválida del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Incrementar el contador de intentos fallidos
                loginAttempts++;

                // Limpiar mensajes de error anteriores
                tvEmailError.setVisibility(View.GONE);
                tvPasswordError.setVisibility(View.GONE);

                // Manejo de errores dependiendo del estado del servidor
                String errorMessage = error.networkResponse != null && error.networkResponse.data != null
                        ? new String(error.networkResponse.data)
                        : "Error en el inicio de sesión";

                if (errorMessage.contains("usuario no encontrado") || errorMessage.contains("no se reconoció")) {
                    tvEmailError.setVisibility(View.VISIBLE);  // Mostrar error de correo
                } else if (errorMessage.contains("contraseña incorrecta")) {
                    tvPasswordError.setVisibility(View.VISIBLE);  // Mostrar error de contraseña
                } else {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }

                // Si el contador alcanza 7 intentos fallidos, bloquear el acceso durante 10 segundos
                if (loginAttempts >= 7) {
                    isBlocked = true;
                    Toast.makeText(getContext(), "Número máximo de intentos alcanzado. Intenta nuevamente en 10 segundos.", Toast.LENGTH_LONG).show();

                    // Reiniciar el bloqueo después de 10 segundos
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isBlocked = false;  // Desbloquear el login después de 10 segundos
                            loginAttempts = 0;  // Resetear el contador de intentos
                            Toast.makeText(getContext(), "Puedes intentar iniciar sesión nuevamente.", Toast.LENGTH_SHORT).show();
                        }
                    }, 10000);  // 10 segundos de espera
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

    public void saveUserProfile(String name, String surname, String email, String phone) {
        profileManager.saveInfo(name, surname, email, phone); // Guardar los datos del usuario
        Toast.makeText(getContext(), "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

