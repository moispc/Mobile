package com.example.food_front;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    public RegisterFragment() {
        // Required empty public constructor
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

        // Agregar el listener al boton de registro
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
        // Controlar que no haya imputs vacios
        if (etNombre.getText().toString().isEmpty() ||
                etApellido.getText().toString().isEmpty() ||
                etCorreo.getText().toString().isEmpty() ||
                etTelefono.getText().toString().isEmpty() ||
                etPassword.getText().toString().isEmpty() ||
                etPassword2.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar longitud de nombre y apellido
        if (etNombre.getText().toString().length() < 3 || etApellido.getText().toString().length() < 3) {
            Toast.makeText(getActivity(), "El nombre y apellido deben tener al menos 3 letras", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar formato de correo electrónico
        if (!isValidEmail(etCorreo.getText().toString())) {
            Toast.makeText(getActivity(), "El correo electrónico no tiene un formato válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar formato de número de teléfono
        if (!isValidPhone(etTelefono.getText().toString())) {
            Toast.makeText(getActivity(), "El número telefónico no tiene un formato válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar la contraseña
        if (!isValidPassword(etPassword.getText().toString())) {
            Toast.makeText(getActivity(), "La contraseña debe tener al menos 4 caracteres y contener al menos un número", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Controlar que las contraseñas coincidan
        if (!etPassword.getText().toString().equals(etPassword2.getText().toString())) {
            Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        return phone.length() >= 10 && phone.matches("\\d+"); // Ajusta la validación según el formato de número que necesites
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 4 && password.matches(".*\\d.*"); // Al menos 4 caracteres y al menos un número
    }

    private void registerUser() {
        String url = "https://backmobile1.onrender.com/appUSERS/register/";

        // Crear el json que se enviará en el body
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

        // Crear el request de volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                        replaceFragment(new SuccessRegistryFragment());  // Navigate to success fragment
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("RegisterFragment", "Error: " + error.toString());
                        if (error.networkResponse != null) {
                            Log.e("RegisterFragment", "Response code: " + error.networkResponse.statusCode);
                            String responseBody = new String(error.networkResponse.data);
                            Log.e("RegisterFragment", "Response body: " + responseBody);
                        }
                        Toast.makeText(getActivity(), "Error en el registro", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Agregar la request a la queue de Volley
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

