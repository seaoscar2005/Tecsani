package com.example.pastiapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Mi_Dispositivo extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button button10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_dispositivo);

        // Inicializar elementos de la barra lateral
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        button10 = findViewById(R.id.button10);

        // Abrir el menú lateral al presionar el botón
        button10.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Acciones del menú lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, Pagina_Inicio.class));
            }else if (id == R.id.nav_pasti) {
                startActivity(new Intent(Mi_Dispositivo.this, Personalizacion_Dispositivo.class));
            } else if (id == R.id.nav_agenda) {
                startActivity(new Intent(Mi_Dispositivo.this, Agenda.class));
            } else if (id == R.id.nav_calendario) {
                startActivity(new Intent(Mi_Dispositivo.this, Calendario.class));
            } else if (id == R.id.nav_medicamentos) {
                startActivity(new Intent(Mi_Dispositivo.this, Medicamentos.class));
            } else if (id == R.id.nav_dispositivo) {
                // Ya estás aquí
            } else if (id == R.id.nav_info) {
                startActivity(new Intent(Mi_Dispositivo.this, Mi_Informacion.class));
            } else if (id == R.id.nav_config) {
                startActivity(new Intent(Mi_Dispositivo.this, Configuracion.class));
            } else if (id == R.id.nav_soporte) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tu-pagina-de-soporte.com")));
            }

            item.setChecked(false);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Ir a personalización del pastillero
        Button btnPersonalizacion = findViewById(R.id.button19);
        btnPersonalizacion.setOnClickListener(v -> {
            Intent intent = new Intent(Mi_Dispositivo.this, Personalizacion_Dispositivo.class);
            startActivity(intent);
        });

        // Mostrar advertencia al cambiar de dispositivo
        Button button18 = findViewById(R.id.button18);
        button18.setOnClickListener(v -> mostrarAlerta());
    }

    private void mostrarAlerta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Advertencia");
        builder.setMessage("Si desea cambiar de dispositivo se eliminará el dispositivo registrado anteriormente y se registrará uno nuevo, ¿desea seguir?");
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            Intent intent = new Intent(Mi_Dispositivo.this, Configurar_Dispositivo.class);
            startActivity(intent);
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
