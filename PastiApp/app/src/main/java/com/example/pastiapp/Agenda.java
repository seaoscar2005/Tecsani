package com.example.pastiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Agenda extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Button button26;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        // Inicialización de la barra lateral
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        button26 = findViewById(R.id.button26); // Botón que abre la barra

        // Abrir el menú al hacer clic en el botón
        button26.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Menú lateral con navegación
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                startActivity(new Intent(this, Pagina_Inicio.class));
            }else if (id == R.id.nav_pasti) {
                startActivity(new Intent(this, Personalizacion_Dispositivo.class));
            } else if (id == R.id.nav_agenda) {
                // Ya estás aquí
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
                Intent navegador = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tu-pagina-de-soporte.com"));
                startActivity(navegador);
            }

            item.setChecked(false); // Para que no se quede seleccionado
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
