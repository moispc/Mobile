package com.example.food_front;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class LoginFragment extends Fragment {


    private EditText etCorreo, etPassword;
    private TextView tvErrorEmail, tvError;
    private ImageView ivTogglePassword;
    private Button btnLogin;
    private TextView tvRegister;

    public LoginFragment() {

    }

    @Override
public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Inicializar las vistas
        etCorreo = view.findViewById(R.id.etCorreo);
        etPassword = view.findViewById(R.id.etPassword);
        tvErrorEmail = view.findViewById(R.id.tvErrorEmail);
        tvError = view.findViewById(R.id.tvError);
        ivTogglePassword = view.findViewById(R.id.ivTogglePassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvRegister = view.findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> {
            if (validateInputs()) {
                performLogin();
            }
        });

        tvRegister.setOnClickListener(v -> replaceFragment(new RegisterFragment()));
        ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility());

        return view;
    }

    private boolean validateInputs() {

        tvErrorEmail.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);

        String email = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tvErrorEmail.setText("Correo no registrado");
            tvErrorEmail.setVisibility(View.VISIBLE);
            return false;
        }


        if (TextUtils.isEmpty(password)) {
            tvError.setText("Contraseña incorrecta");
            tvError.setVisibility(View.VISIBLE);
            return false;
        }

        return true;
    }

    private void performLogin() {
        String url = "https://backmobile1.onrender.com/appUSERS/login/";


        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", etCorreo.getText().toString());
            requestBody.put("password", etPassword.getText().toString());
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
                        Toast.makeText(getActivity(), "Ingreso exitoso", Toast.LENGTH_SHORT).show();

                        replaceFragment(new HomeFragment());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                            tvError.setText("Credenciales inválidas");
                            tvError.setVisibility(View.VISIBLE);
                        } else {
                            tvError.setText("Error en el inicio de sesión. Intente nuevamente.");
                            tvError.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }

    private void togglePasswordVisibility() {
        if (etPassword.getInputType() == 129) { // Contraseña oculta
            etPassword.setInputType(145); // Mostrar contraseña
            ivTogglePassword.setImageResource(android.R.drawable.ic_menu_gallery); // Ojo abierto
        } else {
            etPassword.setInputType(129); // Ocultar contraseña
            ivTogglePassword.setImageResource(android.R.drawable.ic_menu_view); // Ojo cerrado
        }
        etPassword.setSelection(etPassword.getText().length()); // Mantener el cursor al final
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

