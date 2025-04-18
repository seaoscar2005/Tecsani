package com.example.pastiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Mi_Informacion extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button button15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_informacion);

        // Inicializar elementos del menú lateral
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        button15 = findViewById(R.id.button15);

        // Abrir el menú lateral al presionar el botón
        button15.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Acciones del menú
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, Pagina_Inicio.class));
            }else if (id == R.id.nav_pasti) {
                startActivity(new Intent(Mi_Informacion.this, Personalizacion_Dispositivo.class));
            } else if (id == R.id.nav_agenda) {
                startActivity(new Intent(Mi_Informacion.this, Agenda.class));
            } else if (id == R.id.nav_calendario) {
                startActivity(new Intent(Mi_Informacion.this, Calendario.class));
            } else if (id == R.id.nav_medicamentos) {
                startActivity(new Intent(Mi_Informacion.this, Medicamentos.class));
            } else if (id == R.id.nav_dispositivo) {
                startActivity(new Intent(Mi_Informacion.this, Mi_Dispositivo.class));
            } else if (id == R.id.nav_info) {
                // Ya estás aquí
            } else if (id == R.id.nav_config) {
                startActivity(new Intent(Mi_Informacion.this, Configuracion.class));
            } else if (id == R.id.nav_soporte) {
                Intent navegador = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tu-pagina-de-soporte.com"));
                startActivity(navegador);
            }

            item.setChecked(false);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
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
