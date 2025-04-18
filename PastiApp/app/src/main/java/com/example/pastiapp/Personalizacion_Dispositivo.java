package com.example.pastiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Personalizacion_Dispositivo extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button button20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalizacion_dispositivo);

        // Inicialización de Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        button20 = findViewById(R.id.button20);

        // Abrir menú lateral
        button20.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Opciones del menú
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, Pagina_Inicio.class));
            } else if (id == R.id.nav_pasti) {
                // Ya estás aquí
            } else if (id == R.id.nav_agenda) {
                startActivity(new Intent(this, Agenda.class));
            } else if (id == R.id.nav_calendario) {
                startActivity(new Intent(this, Calendario.class));
            } else if (id == R.id.nav_medicamentos) {
                startActivity(new Intent(this, Medicamentos.class));
            } else if (id == R.id.nav_dispositivo) {
                startActivity(new Intent(this, Mi_Dispositivo.class));
            } else if (id == R.id.nav_info) {
                startActivity(new Intent(this, Mi_Informacion.class));
            } else if (id == R.id.nav_config) {
                startActivity(new Intent(this, Configuracion.class));
            } else if (id == R.id.nav_soporte) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tu-pagina-de-soporte.com")));
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
