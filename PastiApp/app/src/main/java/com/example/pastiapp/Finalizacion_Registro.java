package com.example.pastiapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Finalizacion_Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizacion_registro);

        Button btnIrPaginaInicio = findViewById(R.id.button34);
        btnIrPaginaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Finalizacion_Registro.this, Pagina_Inicio.class);
                startActivity(intent);
                finish(); // Opcional: para cerrar esta pantalla y que no puedan regresar con "Back"
            }
        });
    }
}