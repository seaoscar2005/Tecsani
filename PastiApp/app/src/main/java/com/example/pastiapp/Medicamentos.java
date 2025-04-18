package com.example.pastiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Medicamentos extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button button12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);

        // Inicialización de la barra lateral
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        button12 = findViewById(R.id.button12); // Botón que abre el menú

        // Abrir el menú al presionar el botón
        button12.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Manejar clics del menú lateral
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, Pagina_Inicio.class));
            }else if (id == R.id.nav_pasti) {
                startActivity(new Intent(Medicamentos.this, Personalizacion_Dispositivo.class));
            } else if (id == R.id.nav_agenda) {
                startActivity(new Intent(Medicamentos.this, Agenda.class));
            } else if (id == R.id.nav_calendario) {
                startActivity(new Intent(Medicamentos.this, Calendario.class));
            } else if (id == R.id.nav_medicamentos) {
                // Ya estás aquí
            } else if (id == R.id.nav_dispositivo) {
                startActivity(new Intent(Medicamentos.this, Mi_Dispositivo.class));
            } else if (id == R.id.nav_info) {
                startActivity(new Intent(Medicamentos.this, Mi_Informacion.class));
            } else if (id == R.id.nav_config) {
                startActivity(new Intent(Medicamentos.this, Configuracion.class));
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
