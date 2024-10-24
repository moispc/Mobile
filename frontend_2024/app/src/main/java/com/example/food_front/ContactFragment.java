package com.example.food_front;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment {

    private EditText etNombre, etApellido, etEmail, etMensaje;
    private Button btnEnviar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        // Inicializar vistas
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etEmail = view.findViewById(R.id.etEmail);
        etMensaje = view.findViewById(R.id.etMensaje);
        btnEnviar = view.findViewById(R.id.btnEnviar);

        // Agregar listener al botón de Enviar
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar la lógica para enviar el mensaje aquí
                enviarMensaje();
            }
        });

        return view;
    }

    private void enviarMensaje() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String mensaje = etMensaje.getText().toString().trim();
        String regex = "^[a-zA-Z\\s]+$"; // Permite solo letras, números y espacios
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || mensaje.isEmpty()) {
            Toast.makeText(getContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        };
        if (!nombre.matches(regex)) {
            // Mostrar un mensaje de error al usuario
            Toast.makeText(getContext(), "Entrada no válida", Toast.LENGTH_SHORT).show();
        }
        if (nombre.length()>15){
            Toast.makeText(getContext(), "El nombre es demasiado largo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!apellido.matches(regex)) {
            // Mostrar un mensaje de error al usuario
            Toast.makeText(getContext(), "Entrada no válida", Toast.LENGTH_SHORT).show();
        }
        if (apellido.length()<3 || apellido.length()>15){
            Toast.makeText(getContext(), "El apellido debe tener entre más de 3 y  hasta 15 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!email.matches(emailPattern)) {
            Toast.makeText(getContext(), "El email no es válido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mensaje.length()<5 || mensaje.length()>100){
            Toast.makeText(getContext(), "El mensaje  debe contener de 5 a 100 carateres", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mensaje.matches(regex)) {
            // Mostrar un mensaje de error al usuario
            Toast.makeText(getContext(), "El texto del mensaje no es válido", Toast.LENGTH_SHORT).show();
            return;
        }
        // Aquí puedes implementar la lógica para enviar el mensaje (por ejemplo, a través de una API)
        Toast.makeText(getContext(), "Mensaje enviado", Toast.LENGTH_SHORT).show();
    }
}

