package com.example.pastiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button iniciar con correo
        Button btnCorreo = findViewById(R.id.C_Email);
        btnCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Iniciar_Sesion_C.class);
                startActivity(intent);
            }
        });

        // Botón "Crear Cuenta"
        Button btnCrearCuenta = findViewById(R.id.Crear_Cuenta);
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Crear_Cuenta.class);
                startActivity(intent);
            }
        });
    }
}